package com.yudong.function;

import com.yudong.function.model.PersonTest;
import com.yudong.sensitive.SENSITIVE;
import org.junit.Before;
import org.junit.Test;

/**
 * user:zyd
 * date:2016/8/29
 * time:10:53
 * version:1.0.0
 */
public class SENSITIVETest {
    private PersonTest personTest = new PersonTest();

    @Before
    public void testBefore() {
        personTest.setCardNo("1234567890");
        personTest.setName("张玉东");
        personTest.setIdNo("1234567890");
        personTest.setRemark("这只是一个测试类");
    }

    @Test
    public void sensitiveTest() {
        System.out.println(SENSITIVE.sensitiveToJson(personTest));
        System.out.println(personTest);
    }
}
