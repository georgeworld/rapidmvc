/*
 * Author: George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.util;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import gbt.config.GeorgeLoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class RapidHelper {

    private static final GeorgeLogger LOG = GeorgeLoggerFactory.getLogger(RapidHelper.class);

    public static String getJsonString(HttpServletRequest request) {
        try {
            InputStream is = request.getInputStream();
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                StringBuffer strb = new StringBuffer();
                String temp;
                while ((temp = reader.readLine()) != null) {
                    strb.append(temp);
                }
                reader.close();
                return strb.toString();
            } else {
                LOG.error("### Empty data of request input stream.");
                return null;
            }
        } catch (IOException ex) {
            LOG.error("### Exception when reading json string from request.", ex);
            return null;
        }

    }
}
