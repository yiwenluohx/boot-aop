package com.study.bootaop.entity;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * ClassName: dd
 * Description:
 *
 * @Author: luohx
 * Date: 2022/4/18 下午5:58
 * History:
 * <author>          <time>          <version>          <desc>
 * luohx            修改时间           1.0
 */
public class RsaConfig {

    private RSAKey rsaKey;

    /**
     * Gets the value of rsaKey.
     *
     * @return the value of rsaKey
     */
    public RSAKey getRsaKey() {
        return generateRsaKey();
    }

    private RSAKey generateRsaKey() {
        // 从 classpath 下获取 RSA 秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
        // 获取 RSA 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 获取 RSA 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        return rsaKey;
    }
}
