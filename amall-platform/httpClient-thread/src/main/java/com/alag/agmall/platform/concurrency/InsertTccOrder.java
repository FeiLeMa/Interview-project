package com.alag.agmall.platform.concurrency;

import java.io.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InsertTccOrder {
    public static void main(String[] args) throws Exception {
        File file1 = new File("/Users/alag/Desktop/agbao/orderSqlFile");
        File file2 = new File("/Users/alag/Desktop/agbao/orderIDFile");
        long min = 100000000000000000L;
        long max = 999999999999999999L;

        BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1,true), "utf-8"));
        BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2,true), "utf-8"));
        String oID1 = "";
        String oID2 = "";

        for (int i = 0; i < 100; i++) {
            oID1 = "'" + min + (((long) (new Random().nextDouble() * (max - min))))+"'";
            oID2 = "'" + min + (((long) (new Random().nextDouble() * (max - min))))+"'";
            bw2.write(oID2.replaceAll("'",""));
            bw2.newLine();
            String sql = "INSERT INTO ord_order values("+oID1+","+oID2+",'6587',100.00,10,NULL,NOW(),NOW());";
            bw1.write(sql);
            bw1.newLine();
        }


        bw1.flush();
        bw2.flush();
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file2), "utf-8"));
////
////      System.out.println(br.readLine());
//        Map<String, String> paramMap = Maps.newHashMap();
//        String orderNo = "";
//        String tradeNo = "";
//        for (int i = 0; i < 100; i++) {
//            orderNo = br.readLine();
//            tradeNo = min + (((long) (new Random().nextDouble() * (max - min)))) + "";
//            paramMap.put("out_trade_no", orderNo);
//            paramMap.put("trade_no", tradeNo);
//            paramMap.put("trade_status", "20");
//            paramMap.put("gmt_payment", DateTimeUtil.dateToStr(new Date()));
//            alipayService.aliCallback(paramMap);
///*            executorService.submit(new Callable() {
//                @Override
//                public Object call() throws Exception {
//                    return 1;
//                }
//            });
//        }*/
////        executorService.shutdown();
//        }
    }

}

