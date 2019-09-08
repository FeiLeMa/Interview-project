package com.alag.agmall.platform.concurrency;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 多线程-HttpClient连接池管理HTTP请求实例
 */
public class InsertTMessageTest {
    public static void main(String[] args) throws Exception {
        final long min = 100000000000000000L;
        final long max = 999999999999999999L;


        //连接池对象
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        //将最大连接数增加到200
        connectionManager.setMaxTotal(1000);
        //将每个路由的默认最大连接数增加到20
        connectionManager.setDefaultMaxPerRoute(100);
        //添加cookie存储
        CookieStore cookieStore = new BasicCookieStore();
        //HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();


/*        //URIs to DoGet
        String[] urisToGet = {
                "http://localhost:8080/alipay/ali_notify_back",
                "http://localhost:8091/user/get_user_info",
                "http://localhost:8091/user/get_user_info",
                "http://localhost:8097/order/get_cart_product"
        };
        //为每一个URI创建一个线程
        GetThread[] threads = new GetThread[urisToGet.length];
        for (int i=0;i<threads.length;i++){
            HttpGet httpGet = new HttpGet(urisToGet[i]);
            threads[i] = new GetThread(httpClient,httpGet);
        }*/
        //URIs to DoPost
        String uriToPost = "http://localhost:10000/pay_transaction/prestore_message";

        //线程个数
        int threadPool = 2000;

        List<String> ids = Lists.newArrayList();
        List<String> messageIds = Lists.newArrayList();
        for (int i = 0; i < threadPool; i++) {
            messageIds.add(min + ((long)((new Random().nextDouble() * (max - min)))) + "");
            ids.add(min + ((long)((new Random().nextDouble() * (max - min)))) + "");
        }


        PostThread[] threads = new PostThread[threadPool];
        for (int i = 0; i < threadPool; i++) {
            HttpPost httpPost = new HttpPost(uriToPost);

//          设置请求的参数
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id", ids.get(i));
            jsonParam.put("version", 1);
            jsonParam.put("messageBody", "000000000");
            jsonParam.put("consumerQueue", "oooo");
            jsonParam.put("messageSend_times", 0);
            jsonParam.put("messageId", messageIds.get(i));
            jsonParam.put("areadlyDead", 500);
            jsonParam.put("status", 300);
            jsonParam.put("createTime", new Date());
            jsonParam.put("updateTime", new Date());



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