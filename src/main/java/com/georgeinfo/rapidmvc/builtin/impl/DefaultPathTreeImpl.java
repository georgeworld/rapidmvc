/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.builtin.impl;

import com.georgeinfo.rapidmvc.*;
import com.georgeinfo.rapidmvc.api.*;
import com.georgeinfo.rapidmvc.annotation.Path;
import com.georgeinfo.rapidmvc.annotation.Get;
import com.georgeinfo.rapidmvc.annotation.Post;
import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.base.util.string.StringTool;
import com.georgeinfo.rapidmvc.annotation.Delete;
import com.georgeinfo.rapidmvc.annotation.Put;
import com.georgeinfo.rapidmvc.api.ResourceLoader;
import com.georgeinfo.rapidmvc.exception.RapidMvcException;
import com.georgeinfo.rapidmvc.uritree.UriTreeImpl;
import gbt.config.GeorgeLoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

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
                                            HttpMethodEnum httpMethod = null;
                                            //控制器方法名称转换为URI片段后的内容
                                            String methodNamePath = StringTool.humpStringToHyphensString(m.getName());
                                            //控制器方法及URI参数（包括控制器方法注解内的URI参数部分）所对应的URI路径片段
                                            String methodNameAndParametersUri = null;

                                            if (m.isAnnotationPresent(Post.class)) {
                                                httpMethod = HttpMethodEnum.POST;
                                                //POST控制器方法
                                                Post p = m.getAnnotation(Post.class);
                                                if (p.value() != null && !p.value().trim().isEmpty()) {
                                                    methodNameAndParametersUri = p.value();
                                                } else {
                                                    methodNameAndParametersUri = methodNamePath;
                                                }

                                                //注册POST路径
                                                addPath(tree, controllerPath, methodNameAndParametersUri, methodNamePath, controller, m, httpMethod);
                                            }

                                            if (m.isAnnotationPresent(Get.class)) {
                                                httpMethod = HttpMethodEnum.GET;
                                                //GET控制器方法
                                                Get g = m.getAnnotation(Get.class);
                                                if (g.value() != null && !g.value().trim().isEmpty()) {
                                                    methodNameAndParametersUri = g.value();
                                                } else {
                                                    methodNameAndParametersUri = methodNamePath;
                                                }

                                                //注册GET路径
                                                addPath(tree, controllerPath, methodNameAndParametersUri, methodNamePath, controller, m, httpMethod);
                                            }

                                            if (m.isAnnotationPresent(Put.class)) {
                                                httpMethod = HttpMethodEnum.PUT;
                                                //Put控制器方法
                                                Put p = m.getAnnotation(Put.class);
                                                if (p.value() != null && !p.value().trim().isEmpty()) {
                                                    methodNameAndParametersUri = p.value();
                                                } else {
                                                    methodNameAndParametersUri = methodNamePath;
                                                }

                                                //注册PUT路径
                                                addPath(tree, controllerPath, methodNameAndParametersUri, methodNamePath, controller, m, httpMethod);
                                            }

                                            if (m.isAnnotationPresent(Delete.class)) {
                                                httpMethod = HttpMethodEnum.DELETE;
                                                //DELETE控制器方法
                                                Delete d = m.getAnnotation(Delete.class);
                                                if (d.value() != null && !d.value().trim().isEmpty()) {
                                                    methodNameAndParametersUri = d.value();
                                                } else {
                                                    methodNameAndParametersUri = methodNamePath;
                                                }

                                                //注册DELETE路径
                                                addPath(tree, controllerPath, methodNameAndParametersUri, methodNamePath, controller, m, httpMethod);
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

    private boolean isMethodRegistered(HttpMethodEnum restfullType, ControllerWrapper ci, String methodPath) {
        if (restfullType == HttpMethodEnum.GET) {
            return ci.isGetPathRegistered(methodPath);
        } else if (restfullType == HttpMethodEnum.POST) {
            return ci.isPostPathRegistered(methodPath);
        } else {
            throw new RapidMvcException("### Unsupported HTTP request method: isMethodRegistered(...)");
        }
    }

    public boolean addPath(UriTreeImpl tree,
                           String controllerPath,
                           String methodNameAndParametersUri,
                           String methodNamePath,
                           Controller controllerObject,
                           Method method,
                           HttpMethodEnum restfullType) {
        boolean existingUriParameters = false;
        if (!methodNameAndParametersUri.equals(methodNamePath)) {
            existingUriParameters = true;

            if (methodNameAndParametersUri.startsWith("/")) {
                methodNameAndParametersUri = StringUtils.removeStart(methodNameAndParametersUri, "/");
            }
            if (methodNameAndParametersUri.endsWith("/")) {
                methodNameAndParametersUri = StringUtils.removeEnd(methodNameAndParametersUri, "/");
            }

            if (!methodNameAndParametersUri.contains("/") && methodNameAndParametersUri.trim().startsWith("{")) {
                //拒绝@Post("{param}")  这样的单个URI参数的注解
                throw new RapidMvcException("#拒绝@Post(\"{param}\")  这样的单个URI参数的注解");
            }
        }


        ControllerWrapper ci = pathTreeMap.get(controllerPath);
        if (ci == null) {
            ci = new ControllerWrapper(controllerPath, controllerObject);
            boolean r = ci.addMethod(restfullType, methodNameAndParametersUri, methodNamePath, method);
            pathTreeMap.put(controllerPath, ci);

            return r;
        } else { //如果控制器登记不为空，则检查该控制器下的方法是否包含当前待登记的方法
            if (isMethodRegistered(restfullType, ci, methodNameAndParametersUri) == true) {
                logger.error("## RESTful-" + restfullType.name() + " Method path[" + methodNameAndParametersUri + "] has been bind to method:" + method.getName() + " of controller [" + controllerObject.getClass().getName() + "] when add path.");
                return false;
            } else {
                //检测同一个控制器内，可能出现多个注解内URI格式相同的情况
                if (existingUriParameters == true) {
                    //搜索本控制器内已注册的同层方法
                    Map<String, String> methodUriRegisteredMap = ci.getControllerMethodAnnotationUri(restfullType);
                    if (methodUriRegisteredMap != null && !methodUriRegisteredMap.isEmpty()) {
                        String[] uriFragmentsWaitingToBeChecked = methodNameAndParametersUri.split("/");
                        for (Map.Entry<String, String> e : methodUriRegisteredMap.entrySet()) {
                            String[] uriFragmentsOfRegistered = e.getKey().split("/");
                            if (uriFragmentsOfRegistered.length == uriFragmentsWaitingToBeChecked.length) {
                                int i = 0;
                                //是否通过路径重复审核
                                boolean approved = false;
                                for (String urip : uriFragmentsOfRegistered) {
                                    //只要有一层URI片段，不是变量，与已注册的路径同层URI片段不同，则认为这是不同的控制器方法路径
                                    if (!uriFragmentsWaitingToBeChecked[i].contains("{")
                                            && !urip.contains("{")
                                            && !uriFragmentsWaitingToBeChecked[i].trim().equals(urip)) {
                                        approved = true;
                                        break;
                                    }
                                    i++;
                                }

                                //如果在已注册的方法路径中，找到了与当前将要注册的方法路径相同规则URI，则报错
                                if (approved == false) {
                                    throw new RapidMvcException("### 已经有相同规则的控制器方法注册了：" + methodNameAndParametersUri);
                                }
                            }
                        }
                    }
                }

                return ci.addMethod(restfullType, methodNameAndParametersUri, methodNamePath, method);
            }
        }
    }

    @Override
    public boolean addPath(String controllerPath,
                           String methodPath,
                           String methodNamePath,
                           Controller controllerObject,
                           Method method,
                           HttpMethodEnum restfullType) {
        ControllerWrapper ci = pathTreeMap.get(controllerPath);
        if (ci == null) {
            ci = new ControllerWrapper(controllerPath, controllerObject);
            boolean r = ci.addMethod(restfullType, methodPath, methodNamePath, method);
            pathTreeMap.put(controllerPath, ci);

            return r;
        } else {//如果控制器登记不为空，则检查该控制器下的方法是否包含当前待登记的方法
            if (isMethodRegistered(restfullType, ci, methodPath) == true) {
                logger.error("## RESTful-" + restfullType + " Method path[" + methodPath + "] has been bind to method:" + method.getName() + " of controller [" + controllerObject.getClass().getName() + "] when add path.");
                return false;
            } else {
                return ci.addMethod(restfullType, methodPath, methodNamePath, method);
            }
        }
    }

    public ExecutableMethod doMatchingPath(MayBeMatchedController mayBeMatchedController, HttpMethodEnum restfullType) {
        ExecutableMethod result = null;
        ControllerWrapper ci = mayBeMatchedController.getControllerWrapper();
        if (ci != null) {
            String methodNameAndParametersUri = mayBeMatchedController.getMethodNameAndParametersUri();
            if (restfullType == HttpMethodEnum.POST) {//POST请求
                ExecutableMethod em = ci.getMatchedPostMethod(methodNameAndParametersUri);
                if (em != null) {
                    em.setController(ci.getControllerObject());
                    result = em;
                }
            } else if (restfullType == HttpMethodEnum.GET) {//GET请求
                ExecutableMethod em = ci.getMatchedGetMethod(methodNameAndParametersUri);
                if (em != null) {
                    em.setController(ci.getControllerObject());
                    result = em;
                }
            } else {
                throw new RapidMvcException("## Unsupported HTTP request method.");
            }
        }

        if (result != null) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * 创建符合请求条件的控制器匹配对象
     *
     * @param uri            本次请求的URI（从上下文contextPath开始向后，不包括contextPath，不包括query string）
     * @param controllerPath 控制器所对应的uri部分，也是不包括contextPath
     **/
    private MayBeMatchedController createMatchedController(String uri, String controllerPath) {

        if (uri.length() - controllerPath.length() >= 2) {
            String methodNameAndParameterUri = StringUtils.removeStart(uri, controllerPath + "/");
        }

        return new MayBeMatchedController(controllerPath, uri);
    }

    /**
     * 根据URI查询控制器方法
     *
     * @param uri 不包括上下文路径的URI，如：open/article/123，包括URI参数部分
     **/
    public ExecutableMethod doMatchingPath(String uri, HttpMethodEnum restfullType) {
        ExecutableMethod result = null;

        //符合URI条件的所有控制列表
        List<ControllerWrapper> mayBeMatchedControllerList = new ArrayList<ControllerWrapper>();
        for (Map.Entry<String, ControllerWrapper> entry : pathTreeMap.entrySet()) {
            if (uri.startsWith(entry.getKey())) {//找到了控制器path，与当前访问的URI前半部分匹配
                //将符合条件的控制器放入临时列表
                mayBeMatchedControllerList.add(entry.getValue());
            }
        }
        if (!mayBeMatchedControllerList.isEmpty()) {
            ExecutableMethod em = null;
            MayBeMatchedController mayBeMatchedController;
            ControllerWrapper ci = null;
            if (mayBeMatchedControllerList.size() == 1) {
                ci = mayBeMatchedControllerList.get(0);
                //从URI中移除控制器部分
                uri = StringUtils.removeStart(uri, ci.getControllerPath());
                mayBeMatchedController = createMatchedController(uri, ci.getControllerPath());
                mayBeMatchedController.setControllerWrapper(ci);

                //## 开始路径匹配计算 ################################
                em = doMatchingPath(mayBeMatchedController, restfullType);
            } else {//存在多个符合URI前缀的控制器，寻找最长符合的
                //对符合条件的控制器列表，按照控制器path的长短，倒序排序
                Collections.sort(mayBeMatchedControllerList, new Comparator<ControllerWrapper>() {

                    /** 按照控制器Path的长度倒序排序 */
                    @Override
                    public int compare(ControllerWrapper o1, ControllerWrapper o2) {
                        int i = o2.getControllerPath().length() - o1.getControllerPath().length();
                        return i;
                    }
                });

                //循环在匹配规则的控制器类表中，寻找符合方法uri的控制器方法
                for (ControllerWrapper mc : mayBeMatchedControllerList) {
                    ci = mc;

                    //从URI中移除控制器部分
                    String methodNameAndParametersUri = StringUtils.removeStart(uri, ci.getControllerPath());
                    mayBeMatchedController = createMatchedController(methodNameAndParametersUri, ci.getControllerPath());
                    mayBeMatchedController.setControllerWrapper(ci);

                    //## 开始路径匹配计算 ################################
                    em = doMatchingPath(mayBeMatchedController, restfullType);
                    //如果找到了符合条件的控制器方法，就跳出循环，否则就循环完整个符合条件的控制器列表
                    if (em != null) {
                        break;
                    }
                }

                if (em == null) {
                    logger.error("### 错误：找不到匹配的注册控制器方法[1]");
                }
            }

            return em;
        } else {
            logger.error("### 错误：找不到匹配的注册控制器【2】");
            return null;
        }
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
