package com.yudong.function.model;

import com.yudong.sensitive.annotation.SENSITIVEField;
import com.yudong.sensitive.enummodel.SensitiveType;

/**
 * user:zyd
 * date:2016/8/29
 * time:11:24
 * version:1.0.0
 */
public class PersonTest {
    @SENSITIVEField(type = SensitiveType.CHINESE_NAME)
    private String name;
    @SENSITIVEField(type = SensitiveType.ID_CARD)
    private String idNo;
    @SENSITIVEField(type = SensitiveType.BANK_CARD)
    private String cardNo;
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    @Override
    public String toString() {
        return "PersonTest{" +
                "name='" + name + '\'' +
                ", idNo='" + idNo + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
