/*
 * Author: George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (www.georgeinfo.com), All Rights Reserved.
 */
package test;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.georgeinfo.rapidmvc.annotation.Param;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主要功能测试入口
 *
 * @author George <Georgeinfo@163.com>
 */
public class MainTest {

    public MainTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public void testAnnotation(String param1, @Param("fuck") int param2) {

    }

    @Test
    public void name() throws Exception {
    }

    // 单元测试方法在下面写
    @Test
    public void hello() throws NoSuchMethodException {
        String url = "home/{doType}/{p999}/8908/show";
        String regex = "home\\/\\{(\\w+)\\}\\/\\{(\\w+)\\}\\/8908\\/show";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(url);
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
