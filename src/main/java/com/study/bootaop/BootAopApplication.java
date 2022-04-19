package com.study.bootaop;

import com.study.bootaop.entity.RsaConfig;
import com.study.bootaop.utils.BearerTokenUtils;
import com.study.bootaop.utils.JWTUtils;
import com.study.bootaop.utils.UUID;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BootAopApplication {

    public static void main(String[] args) {

        try {
            Map<String, Object> claims = new HashMap<>(16);
            //发行人
            claims.put("iss", "boot-aop");
            String token = JWTUtils.createToken(new RsaConfig().getRsaKey().toPrivateKey(), "100008926", claims, 30);
            if (JWTUtils.validTime(token) > 0L && JWTUtils.verify(new RsaConfig().getRsaKey().toRSAPublicKey(), token)) {
                String tok = "Bearer ".concat(token);
                String aut = BearerTokenUtils.getToken(tok);
            }
        } catch (Exception ex) {

        }


        SpringApplication.run(BootAopApplication.class, args);
    }

}
