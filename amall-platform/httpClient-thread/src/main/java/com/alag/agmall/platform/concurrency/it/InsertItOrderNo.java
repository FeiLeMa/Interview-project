package com.alag.agmall.platform.concurrency.it;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

public class InsertItOrderNo {
    public static void main(String[] args) throws Exception {
        File file1 = new File("/Users/alag/Desktop/interview-pj/orderSqlFile");
        File file2 = new File("/Users/alag/Desktop/interview-pj/orderIDFile");
        long min = 100000000000L;
        long max = 999999999999L;

        BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1,true), "utf-8"));
        BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2,true), "utf-8"));
        String oID1 = "";
        String oID2 = "";

        for (int i = 0; i < 300; i++) {
            oID1 = "'" + min + (((long) (new Random().nextDouble() * (max - min))))+"'";
            oID2 = oID1;
            bw2.write(oID2.replaceAll("'",""));
            bw2.newLine();
            String sql = "INSERT INTO amall_order (`order_no`,`user_id`,`shipping_id`,`payment`,`payment_type`,`postage`,`status`,`payment_time`,`send_time`,`end_time`,`close_time`,`create_time`,`update_time`)VALUES("+oID1+",1,1,100.00,1,0,10,NOW(),NULL,NULL,NULL,NOW(),NOW());";
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
