package com.study.bootaop.utils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.study.bootaop.enums.ResultType;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * ClassName: JWTUtils
 * Description:
 * @Author: luohx
 * Date: 2022/4/19 上午10:00
 * History:
 * <author>          <time>          <version>          <desc>
 * luohx            修改时间           1.0
 */
public abstract class JWTUtils {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    public static String createToken(PrivateKey privateKey, String subjectId, Map<String, Object> claims, int minutes) {
        try {
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.RS256);
            JWTClaimsSet claimsSet = buildClaimsSet(subjectId, claims, minutes);
            SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
            JWSSigner jwsSigner = new RSASSASigner(privateKey);
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static JWTClaimsSet buildClaimsSet(String subjectId, Map<String, Object> claims, int minutes) {
        Date now = Calendar.getInstance().getTime();
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                // 面向的用户
                .subject(subjectId)
                // 签发时间
                .issueTime(now)
                // 过期时间
                .expirationTime(DateUtils.addMinutes(now, minutes))
                .jwtID(UUID.random());

        for (String key : claims.keySet()) {
            builder.claim(key, claims.get(key));
        }
        return builder.build();
    }

    public static JWTClaimsSet parseClaimsSet(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            throw new UnsupportedOperationException(ResultType.TOKEN_INVALID.message(), e);
        }
    }

    /**
     * 获取用户ID
     *
     * @param token
     * @return
     */
    public static long getSubjectId(String token) {
        JWTClaimsSet claims = parseClaimsSet(token);
        String subject = claims.getSubject();
        return NumberUtils.toLong(subject);
    }


    /**
     * 获取剩余有效时间（毫秒数）
     * 正数表示在有效期内，负数表示已过有效时间
     * @param token
     * @return
     */
    public static long validTime(String token) {
        JWTClaimsSet claims = JWTUtils.parseClaimsSet(token);
        Date date = claims.getExpirationTime();
        if (date == null) {
            return 0L;
        }
        return date.getTime() - System.currentTimeMillis();
    }


    /**
     * 验证 token 是否有效
     *
     * @param publicKey
     * @param token
     * @return
     */
    public static boolean verify(RSAPublicKey publicKey, String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(publicKey);
            return signedJWT.verify(verifier);
        } catch (Exception e) {
            logger.error("验证token异常", e);
        }
        return false;
    }
}
