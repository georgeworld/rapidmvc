/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.support;

import com.georgeinfo.base.definition.returnobject.SimpleReturnObject;
import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.base.util.script.JsBuilder;
import gbt.config.GeorgeLoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 文件上传器
 *
 * @author George <Georgeinfo@163.com>
 */
public class FileUploader {

    /**
     * 上传文件所在目录的路径
     */
    private String uploadDir = "/upload";

    /**
     * 如果不设置 {uploadDir}，则使用默认路径
     */
    public FileUploader() {
    }

    /**
     * @param uploadDir 上传文件所在目录的路径
     */
    public FileUploader(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(FileUploader.class);

    public SimpleReturnObject<List<FileUpCallback>> upload(HttpServletRequest request) {
        SimpleReturnObject<List<FileUpCallback>> result = new SimpleReturnObject<List<FileUpCallback>>();

        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String uploadFilePath = request.getServletContext().getRealPath(uploadDir);
        File file = new File(uploadFilePath);
        //判断上传文件的保存目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            logger.error("## Dir [" + uploadDir + "] 不存在，请先创建");
            //创建目录
            //file.mkdir();
            return result.error().addMessage(JsBuilder.begin().addAlert("【" + uploadDir + "】文件夹不存在，请先创建").end());
        }

        //消息提示
        try {
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            //3、判断提交上来的数据是否是上传表单的数据
            if (!ServletFileUpload.isMultipartContent(request)) {
                //按照传统方式获取数据
                return result.error().addMessage(JsBuilder.begin().addAlert("没有接收到文件，请检查from表单的enctype属性，是否是multipart/form-data类型。").end());
            }

            List<FileUpCallback> fcList = new ArrayList<FileUpCallback>();
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
            for (FileItem item : list) {
                //如果fileitem中封装的是普通输入项的数据
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                    logger.info("## upload form field:" + name + "=" + value);
                } else {//如果fileitem中封装的是上传文件
                    //得到上传的文件名称，
                    String filename = item.getName();
                    String originalFileName = filename;
                    System.out.println(filename);
                    if (filename == null || filename.trim().isEmpty()) {
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    if (filename.contains("\\")) {
                        filename = filename.substring(filename.lastIndexOf("\\") + 1);
                    } else if (filename.contains("/")) {
                        filename = filename.substring(filename.lastIndexOf("/") + 1);
                    }
                    //获取item中的上传文件的输入流
                    InputStream in = item.getInputStream();
                    //创建一个文件输出流
                    FileOutputStream out = new FileOutputStream(uploadFilePath + "\\" + filename);
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int len = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while ((len = in.read(buffer)) > 0) {
                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                        out.write(buffer, 0, len);
                    }
                    //关闭输入流
                    in.close();
                    //关闭输出流
                    out.close();
                    //删除处理文件上传时生成的临时文件
                    item.delete();

                    FileUpCallback fileUpCallback = new FileUpCallback(originalFileName, filename);
                    fcList.add(fileUpCallback);
                }
            }

            if (!fcList.isEmpty()) {
                return result.success().addReturnValue(fcList);
            } else {
                return result.empty();
            }
        } catch (FileUploadException e) {
            logger.error("## 文件上传失败", e);
            return result.exception().addMessage("文件上传失败:" + e.getMessage());
        } catch (IOException e) {
            logger.error("## 文件上传失败", e);
            return result.exception().addMessage("文件上传失败:" + e.getMessage());
        }
    }
}
