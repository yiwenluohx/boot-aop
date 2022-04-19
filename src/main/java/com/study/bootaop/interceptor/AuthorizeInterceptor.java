package com.study.bootaop.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.study.bootaop.config.RequestWrapper;
import com.study.bootaop.utils.BearerTokenUtils;
import com.study.bootaop.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @Author: luohx
 * @Description: 权限拦截器
 * @Date: 2021/12/1 上午10:32
 */
public class AuthorizeInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeInterceptor.class);

    /**
     * 上下文对象实例
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            Long userId = getUserId(request);
            //接收访问的目标方法信息
            HandlerMethod h = (HandlerMethod) handler;
            //获取目标方法上的指定注解
            Authorize annotation = h.getMethodAnnotation(Authorize.class);
            if (null != annotation) {
                // 企业ID
                Long eid = getEid(request, annotation);
                // urlPath
                String urlPath = getUriPath(request);
                // 注解鉴权类型
                Authorize.Type annotationType = annotation.type();
                logger.info("权限拦截校验，请求参数 --> 请求路径：{}，urlPath：{}，annotationType：{}，userId：{}，eid：{}", request.getRequestURL(), urlPath, annotationType, userId, eid);
                // 判断相应权限操作
                if (null == userId || null == eid) {
                    throw new AuthException(String.format("缺少鉴权参数，userId:%s,eid:%s", userId, eid));
                }
                //根据annotationType做具体业务的判断
                if (annotationType.equals(Authorize.Type.OWNER)) {
                    logger.info("权限拦截校验 -- 场景owner权限校验");
                    // TODO：判断当前用户是否是场景owner（调用方法校验）
                }
            }
        }
        return true;
    }

    /**
     * 获取uriPath
     *
     * @param request
     * @return
     */
    protected String getUriPath(HttpServletRequest request) {
        return request.getHeader("uri-path");
    }


    /**
     * 获取token
     *
     * @param request
     * @return
     */
    protected String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return BearerTokenUtils.getToken(authorization);
    }

    /**
     * 获取userId
     *
     * @param request
     * @return
     */
    protected Long getUserId(HttpServletRequest request) {
        try {
            return JWTUtils.getSubjectId(getToken(request));
        } catch (Exception e) {
            logger.error("[AuthorizeInterceptor]->获取用户失败,异常信息:{}", e.getMessage());
        }
        return 1L;
    }

    protected Long getEid(HttpServletRequest request, Authorize annotation) {
        //优先使用eid表达式
        if (StringUtils.isNotBlank(annotation.eidExpress())) {
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = (StandardEvaluationContext) context(request);
            context.setBeanResolver(new BeanFactoryResolver(applicationContext));
            String expression = parseSpEl(request, annotation.eidExpress());
            return parser.parseExpression(expression).getValue(context, Long.class);
        } else if (StringUtils.isNotBlank(annotation.eid())) {
            return Long.parseLong(parseSpEl(request, annotation.eid()));
        } else {
            //eid和表达式都不传递则报错
            throw new UnsupportedOperationException("企业id未能成功获取");
        }
    }

    /**
     * 解析parseSpEl
     *
     * @param request
     * @param spEl
     * @return
     */
    private String parseSpEl(HttpServletRequest request, String spEl) {
        ExpressionParser expressionParser = new SpelExpressionParser();
        EvaluationContext context = context(request);
        return expressionParser.parseExpression(spEl).getValue(context, String.class);
    }

    /**
     * 得到EvaluationContext
     *
     * @param request
     * @return
     * @version 1.0
     */
    private EvaluationContext context(HttpServletRequest request) {
        EvaluationContext context = new StandardEvaluationContext();
        Map<String, String[]> args = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entry = args.entrySet();
        Iterator<Map.Entry<String, String[]>> it = entry.iterator();
        while (it.hasNext()) {
            Map.Entry<String, String[]> me = it.next();
            String key = me.getKey();
            String value = me.getValue()[0];
            context.setVariable(key, value);
        }
        parseBodyParam(request, context);
        return context;
    }

    /**
     * 解析RequestBody
     *
     * @param request
     * @param context
     * @version 1.0
     */
    private void parseBodyParam(HttpServletRequest request, EvaluationContext context) {
        String method = request.getMethod().toUpperCase();
        String type = request.getContentType();
        if ("POST".equals(method) && (type.toUpperCase(Locale.ROOT).contains("application/json".toUpperCase()))) {
            RequestWrapper requestWrapper = new RequestWrapper(request);
            String body = requestWrapper.getBodyString();
            JSONObject jsonObject = JSONObject.parseObject(body, Feature.OrderedField);
            for (Map.Entry entry : jsonObject.entrySet()) {
                context.setVariable(entry.getKey().toString(), entry.getValue());
            }
        }
    }
}
