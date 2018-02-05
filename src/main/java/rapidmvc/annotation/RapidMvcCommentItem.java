/*
 * Programming by: George <GeorgeNiceWorld@gmail.com>
 * Copyright (C) George And George Companies to Work For, All Rights Reserved.
 */
package rapidmvc.annotation;

import java.lang.annotation.*;

/**
 *
 * @author George <GeorgeNiceWorld@gmail.com>
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
public @interface RapidMvcCommentItem {

    public String dateTime();

    public String version();

    public String[] notes() default {""};
}
