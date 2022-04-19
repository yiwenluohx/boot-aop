package com.study.bootaop.utils;

/**
 * ClassName: UUID
 * Description:
 * Author: luohx
 * Date: 2022/4/19 上午10:13
 * History:
 * <author>          <time>          <version>          <desc>
 * luohx            修改时间           1.0
 */
public abstract class UUID {

    public static String random() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

}