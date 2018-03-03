/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package test;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String packAndClassName = "test.controller.TestMain";
        String[] at = packAndClassName.split("[.]");
        for (String a : at) {
            System.out.println("[" + a + "]");
        }
    }

}
