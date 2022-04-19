package com.study.bootaop.entity.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * ClassName: ReceiveBillInfo
 * Description:
 *
 * @Author: luohx
 * Date: 2022/4/18 下午5:59
 * History:
 * <author>          <time>          <version>          <desc>
 * luohx            修改时间           1.0
 */
@Document(value = "receive_bill_info")
public class ReceiveBillInfo implements Serializable {
    @Id
    private String id;

    @Field("receive_no")
    private String receiveNo;

    /**
     * Gets the value of id.
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id. *
     * <p>You can use getId() to get the value of id</p>
     * * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the value of receiveNo.
     *
     * @return the value of receiveNo
     */
    public String getReceiveNo() {
        return receiveNo;
    }

    /**
     * Sets the receiveNo. *
     * <p>You can use getReceiveNo() to get the value of receiveNo</p>
     * * @param receiveNo receiveNo
     */
    public void setReceiveNo(String receiveNo) {
        this.receiveNo = receiveNo;
    }
}
