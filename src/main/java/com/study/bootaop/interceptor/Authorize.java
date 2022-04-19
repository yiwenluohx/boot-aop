package com.study.bootaop.interceptor;

import java.lang.annotation.*;

/**
 * @Author: luohx
 * @Description: 权限自定义注解（利用自定义注解实现权限验证）
 * @Date: 2021/12/1 上午10:07
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited //类继承关系中，子类会继承父类使用的注解中被@Inherited修饰的注解; 接口继承关系中，子接口不会继承父接口中的任何注解，不管父接口中使用的注解有没有被@Inherited修饰; 类实现接口时不会继承任何接口中定义的注解
@Documented //这个注解应该被 javadoc工具记录(是一个标记注解)
public @interface Authorize {
    enum Type {
        /**
         * 场景owner
         */
        OWNER,
        /**
         * 单据详情
         */
        BILL_DETAIL,
        /**
         * 单据操作
         */
        BILL_ACTION,
        /**
         * 其他场景
         */
        OTHER
    }

    Type type() default Type.OWNER;

    /**
     * 单据所属企业ID
     * 无eidExpress有效
     *
     * @return
     */
    String eid() default "";

    /**
     * 获取eid的表达式
     * 优先级比eid高
     *
     * @return
     */
    String eidExpress() default "";

    /**
     * 单据ID
     *
     * @return
     */
    String dataId() default "";

    /**
     * 异常信息
     * @return
     */
    String message() default "暂无权限，请联系管理员";
}
