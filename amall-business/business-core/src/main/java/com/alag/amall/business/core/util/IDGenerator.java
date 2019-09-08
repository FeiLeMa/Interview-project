package com.alag.amall.business.core.util;

import java.util.Date;
import java.util.UUID;

public class IDGenerator {

    public static String msgIDBuilder() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String tMIDBuilder() {
        return UUID.randomUUID().toString().replaceAll("-","")+ "-" + System.currentTimeMillis();
    }

    public static String alipayInfoIDBuilder() {
        return IDGenerator.msgIDBuilder();
    }
    public static void main(String[] args) {
//        System.out.println(IDGenerator.msgIDBuilder());
        System.out.println(DateTimeUtil.dateToStr(new Date()));
        System.out.println(IDGenerator.tMIDBuilder());
    }

}
