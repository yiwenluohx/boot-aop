package com.study.bootaop.controller;

import com.study.bootaop.entity.mongo.ReceiveBillInfo;
import com.study.bootaop.interceptor.Authorize;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: TestController
 * Description:
 *
 * @Author: luohx
 * Date: 2022/4/18 下午3:14
 * History:
 * <author>          <time>          <version>          <desc>
 * luohx            修改时间           1.0
 */
@Validated
@RequestMapping("/test")
@RestController
public class TestController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @Authorize(type = Authorize.Type.OWNER, eid = "#eid")
    public ResponseEntity getInfo(@NotNull(message = "企业id为空") @RequestParam("eid") Long eid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(67786120213037229L));
        ReceiveBillInfo info = mongoTemplate.findOne(query, ReceiveBillInfo.class);
        return ResponseEntity.ok(info);
    }
}
