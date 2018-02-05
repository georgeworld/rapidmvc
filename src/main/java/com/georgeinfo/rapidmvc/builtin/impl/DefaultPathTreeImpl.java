/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.builtin.impl;

import com.georgeinfo.rapidmvc.api.*;
import com.georgeinfo.rapidmvc.annotation.Path;
import com.georgeinfo.rapidmvc.annotation.Get;
import com.georgeinfo.rapidmvc.annotation.Post;
import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.base.util.string.StringTool;
import com.georgeinfo.rapidmvc.Constants;
import com.georgeinfo.rapidmvc.Controller;
import com.georgeinfo.rapidmvc.ControllerMethod;
import com.georgeinfo.rapidmvc.ControllerWrapper;
import com.georgeinfo.rapidmvc.HttpMethodEnum;
import com.georgeinfo.rapidmvc.annotation.Delete;
import com.georgeinfo.rapidmvc.annotation.Put;
import com.georgeinfo.rapidmvc.api.ResourceLoader;
import com.georgeinfo.rapidmvc.uritree.MultiwayTree;
import com.georgeinfo.rapidmvc.uritree.UriTreeImpl;
import gbt.config.GeorgeLoggerFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.georgeinfo.rapidmvc.uritree.UriTreeCreator;
import com.georgeinfo.rapidmvc.uritree.UriTreeNode;
import org.apache.commons.lang3.StringUtils;

/**
 * 控制器类URI路径树构建类
 *
 * @author George <GeorgeNiceWorld@gmail.com>
 */
public final class DefaultPathTreeImpl implements PathTree {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(DefaultPathTreeImpl.class);
    /**
     * 控制器path路径与控制器实例对象之间的映射map,key=@Path注解在控制器类上注册的path路径，value=控制器类实例对象
     */
    private final Map<String, ControllerWrapper> pathTreeMap = new HashMap<String, ControllerWrapper>();
    /**
     * 资源加载器
     */
    private ResourceLoader resourceLoader;
    /**
     * URI节点树容器
     */
    private UriTreeImpl uriTreeImpl = null;

    public DefaultPathTreeImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
//        init();
    }

    // 线程安全的惰性加载单例模式 开始
//    private static class PathTreeHolder {
//
//        private static final PathTree instance = new PathTree();
//    }
//
//    public static PathTree getInstance() {
//        return PathTreeHolder.instance;
//    }
    public void addTreeNode(UriTreeNode node, String uriFragment) {

    }

    // 线程安全的惰性加载单例模式 结束
    @Override
    public PathTree init() {
        //扫描控制器包下的所有控制器类，并注册到pathTreeMap中去
        String controllerPath = Constants.getInstance().getControllerPackagePath();
        final Set<Class<?>> classSet = resourceLoader.getClassesByPackage(controllerPath); // ClassScanner.getClasses(Constants.getInstance().getControllerPackagePath());
        if (classSet != null && !classSet.isEmpty()) {
            //创建树形结构容器
            uriTreeImpl = new UriTreeImpl();

            //构建树形结构
            uriTreeImpl.buildTree(new UriTreeCreator() {
                @Override
                public boolean doCreateTree(UriTreeImpl tree) {
                    for (Class<?> clazz : classSet) {
                        if (clazz.isAnnotationPresent(Path.class)) {
                            String controllerPath = null;
                            //判断控制器的@Path注解内，是否自定义了该控制器的URI路径段
                            Path pathAnnotation = clazz.getAnnotation(Path.class);
                            String curi = pathAnnotation.value();
                            if (curi == null || curi.isEmpty()) {
                                //如果Path.clss注解内的URI配置为空，则使用控制器类的包路径+控制器类名称，作为第一部分URI片段
                                String fullClassName = clazz.getName();
                                //移除固定前置包路径
                                StringUtils.removeStart(fullClassName, Constants.getInstance().getControllerPackagePath());

                                String[] packAndClassName = fullClassName.split("[.]");
                                if (packAndClassName.length > 1) {
                                    //循环将每一段包路径，及最后的控制器类名称， 组成树节点
                                    for (String uriFragment : packAndClassName) {
                                        UriTreeNode node = new UriTreeNode();
                                        node.setName(uriFragment);

                                    }
                                } else {
                                    //没有包路径，只把控制器类名称，包装成树节点
                                }

                                String controllerClassName = clazz.getSimpleName();
                                if (controllerClassName.endsWith(Constants.suffix_of_controller)) {
                                    String cidname = controllerClassName.substring(0, controllerClassName.length() - Constants.suffix_of_controller.length());
                                    controllerPath = StringTool.humpStringToHyphensString(cidname);
                                } else {
                                    controllerPath = StringTool.humpStringToHyphensString(controllerClassName);
                                }
                            } else {
                                controllerPath = curi;
                            }

                            //从控制器类中，获得所有的方法
                            Method[] controllerMethods = clazz.getMethods();
                            if (controllerMethods != null && controllerMethods.length > 0) {
                                for (Method m : controllerMethods) {
                                    if (m.isAnnotationPresent(Post.class)
                                            || m.isAnnotationPresent(Get.class)) {
                                        Controller controller = null;
                                        try {
                                            controller = (Controller) clazz.newInstance();
                                        } catch (InstantiationException ex) {
                                            logger.error("## InstantiationException when create new instance of Class [" + clazz.getName() + "]." + ex);
                                        } catch (IllegalAccessException ex) {
                                            logger.error("## IllegalAccessException when create new instance of Class [" + clazz.getName() + "]." + ex);
                                        }

                                        if (controller != null) {
                                            String methodPath = StringTool.humpStringToHyphensString(m.getName());
                                            if (m.isAnnotationPresent(Post.class)) {
                                                //注册POST控制器方法
                                                addPath(tree, controllerPath, methodPath, controller, m, HttpMethodEnum.POST);
                                            }
                                            if (m.isAnnotationPresent(Get.class)) {
                                                //注册GET控制器方法
                                                addPath(tree, controllerPath, methodPath, controller, m, HttpMethodEnum.GET);
                                            }
                                            if (m.isAnnotationPresent(Put.class)) {
                                                //注册Put控制器方法
                                                addPath(tree, controllerPath, methodPath, controller, m, HttpMethodEnum.PUT);
                                            }
                                            if (m.isAnnotationPresent(Delete.class)) {
                                                //注册DELETE控制器方法
                                                addPath(tree, controllerPath, methodPath, controller, m, HttpMethodEnum.DELETE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    return true;
                }

                @Override
                public boolean doCheckTree(UriTreeImpl multiwayTree) {
                    return false;
                }
            });

        }

        return this;
    }

    public Map<String, ControllerWrapper> getPathTreeMap() {
        return pathTreeMap;
    }

    public UriTreeImpl getUriTree() {
        return uriTreeImpl;
    }

    public boolean addPath(UriTreeImpl tree, String controllerPath, String methodPath, Controller controllerObject, Method method, HttpMethodEnum restfullType) {
        ControllerWrapper ci = pathTreeMap.get(controllerPath);
        if (ci == null) {
            ci = new ControllerWrapper(controllerObject);
            boolean r = ci.addMethod(restfullType, methodPath, method);
            pathTreeMap.put(controllerPath, ci);

            return r;
        } else //如果控制器登记不为空，则检查该控制器下的方法是否包含当前待登记的方法
        if (restfullType == HttpMethodEnum.POST) {//POST方法
            if (ci.isPostPathRegistered(methodPath)) {
                logger.error("## RESTful-Post Method path[" + methodPath + "] has been bind to method:" + method.getName() + " of controller [" + controllerObject.getClass().getName() + "] when add path.");
                return false;
            } else {
                return ci.addMethod(restfullType, methodPath, method);
            }
        } else//GET方法
        if (ci.isGetPathRegistered(methodPath)) {
            logger.error("## RESTful-Get Method path[" + methodPath + "] has been bind to method:" + method.getName() + " of controller [" + controllerObject.getClass().getName() + "] when add path.");
            return false;
        } else {
            return ci.addMethod(restfullType, methodPath, method);
        }
    }

    @Override
    public boolean addPath(String controllerPath, String methodPath, Controller controllerObject, Method method, HttpMethodEnum restfullType) {
        ControllerWrapper ci = pathTreeMap.get(controllerPath);
        if (ci == null) {
            ci = new ControllerWrapper(controllerObject);
            boolean r = ci.addMethod(restfullType, methodPath, method);
            pathTreeMap.put(controllerPath, ci);

            return r;
        } else //如果控制器登记不为空，则检查该控制器下的方法是否包含当前待登记的方法
        if (restfullType == HttpMethodEnum.POST) {//POST方法
            if (ci.isPostPathRegistered(methodPath)) {
                logger.error("## RESTful-Post Method path[" + methodPath + "] has been bind to method:" + method.getName() + " of controller [" + controllerObject.getClass().getName() + "] when add path.");
                return false;
            } else {
                return ci.addMethod(restfullType, methodPath, method);
            }
        } else//GET方法
        if (ci.isGetPathRegistered(methodPath)) {
            logger.error("## RESTful-Get Method path[" + methodPath + "] has been bind to method:" + method.getName() + " of controller [" + controllerObject.getClass().getName() + "] when add path.");
            return false;
        } else {
            return ci.addMethod(restfullType, methodPath, method);
        }
    }

    public ControllerMethod doMatchingPath(String controllerPath, String methodPath, HttpMethodEnum restfullType) {
        ControllerMethod result = null;
        ControllerWrapper ci = pathTreeMap.get(controllerPath);
        if (ci != null) {
            if (restfullType == HttpMethodEnum.POST) {//POST请求
                Method m = ci.getMatchedPostMethod(methodPath);
                if (m != null) {
                    result = new ControllerMethod(ci.getControllerObject(), m);
                    return result;
                }
            } else {//GET请求
                Method m = ci.getMatchedGetMethod(methodPath);
                if (m != null) {
                    result = new ControllerMethod(ci.getControllerObject(), m);
                    return result;
                }
            }
        }

        return result;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
