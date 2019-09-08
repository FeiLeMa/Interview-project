package com.alag.agmall.platform.concurrency.it;

import com.alag.agmall.platform.concurrency.util.DateTimeUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 多线程-HttpClient连接池管理HTTP请求实例
 */
public class MessageTest {
    public static void main(String[] args) throws Exception {
        File file = new File("/Users/alag/Desktop/interview-pj/orderIDFile");
        final long min = 100000000000000000L;
        final long max = 999999999999999999L;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));

        //连接池对象
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        //将最大连接数增加到200
        connectionManager.setMaxTotal(200);
        //将每个路由的默认最大连接数增加到20
        connectionManager.setDefaultMaxPerRoute(20);
        //HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        //URIs to DoPost
        String uriToPost = "http://localhost:9092/amall/pay/notify_alipay_callback_test";

        //线程个数
        int threadPool = 300;

//        存放orderNo
        List<String> orderIds = Lists.newArrayList();
        for (int i = 0; i < threadPool; i++) {
            orderIds.add(br.readLine());
        }


//        存放tradeNo
        List<String> tradeNos = Lists.newArrayList();
        for (int i = 0; i < threadPool; i++) {
            tradeNos.add(min + ((long)((new Random().nextDouble() * (max - min)))) + "");
        }


        PostThread[] threads = new PostThread[threadPool];
        for (int i = 0; i < threadPool; i++) {
            HttpPost httpPost = new HttpPost(uriToPost);

//          设置请求的参数
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("out_trade_no", orderIds.get(i));
            jsonParam.put("trade_no", tradeNos.get(i));
            jsonParam.put("trade_status", "TRADE_SUCCESS");
            jsonParam.put("gmt_payment", DateTimeUtil.dateToStr(new Date()));


            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");

            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            threads[i] = new PostThread(httpClient, httpPost);
        }
        //启动线程
        for (int j = 0; j < threads.length; j++) {
            threads[j].start();
        }
        //join 线程
        for (int k = 0; k < threads.length; k++) {
            try {
                threads[k].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 执行Post请求线程
     */
    public static class PostThread extends Thread {
        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpPost httpPost;

        public PostThread(CloseableHttpClient httpClient, HttpPost httpPost) {
            this.httpClient = httpClient;
            this.context = HttpClientContext.create();
            this.httpPost = httpPost;
        }

        @Override
        public void run() {
            try {
                CloseableHttpResponse response = httpClient.execute(httpPost, context);
                try {
                    HttpEntity entity = response.getEntity();
                    String content = EntityUtils.toString(entity, "UTF-8");
                    System.out.println(content);
                } finally {
                    response.close();
                }
            } catch (ClientProtocolException ex) {
                //处理客户端协议异常
            } catch (IOException ex) {
                //处理客户端IO异常
            }
        }
    }
}