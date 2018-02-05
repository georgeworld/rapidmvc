/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.support;

import com.georgeinfo.base.definition.returnobject.SimpleReturnObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class AcHelper {

    private final HttpServletRequest request;

    public AcHelper(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 得到指定前缀的输入框的值
     *
     * @param fieldPrefix 输入框前缀数组，如：new String[]{"attachmentOfileName",
     * "attachmentTfileName", "attachmentNote"}
     * @return key=数据集唯一标示，value=一行数据集的key=value map
     */
    public Map<String, HashMap<String, String>> getDataMapByFieldPrefix(String[] fieldPrefix) {
        Map<String, String[]> paramsMap = request.getParameterMap();
        Map<String, HashMap<String, String>> dataMap = new HashMap<String, HashMap<String, String>>();
        if (paramsMap != null && !paramsMap.isEmpty()) {
            for (Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
                for (String fieldKey : fieldPrefix) {
                    if (entry.getKey().startsWith(fieldKey)) {
                        String id = entry.getKey().substring(fieldKey.length());
                        HashMap<String, String> dm = dataMap.get(id);
                        if (dm == null) {
                            dm = new HashMap<String, String>();
                            dataMap.put(id, dm);
                        }
                        dm.put(fieldKey, entry.getValue()[0]);
                        break;
                    }
                }
            }
        }
        return dataMap;
    }

    /**
     * 上传文件
     */
    public SimpleReturnObject<List<FileUpCallback>> upload() {
        FileUploader uploader = new FileUploader();
        return uploader.upload(request);
    }
}
