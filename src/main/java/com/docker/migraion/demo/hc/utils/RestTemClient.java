package com.docker.migraion.demo.hc.utils;


import com.docker.migraion.demo.hc.model.HttpDeleteWithBody;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @program: 5GSlicing
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-08-19 14:40
 */
@Log4j2
public class RestTemClient {

    public static String doDelete(String data, String url) throws IOException {
        CloseableHttpClient client = null;
        HttpDeleteWithBody httpDelete = null;

        String result = null;

        try {
            client = HttpClients.createDefault();
            httpDelete = new HttpDeleteWithBody(url);

            httpDelete.addHeader("Content-type","application/json; charset=utf-8");
            httpDelete.setHeader("Accept", "application/json; charset=utf-8");
            httpDelete.setEntity(new StringEntity(data));

            CloseableHttpResponse response = client.execute(httpDelete);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);

            if (200 == response.getStatusLine().getStatusCode()) {
                log.debug("远程调用成功.msg={}", result);
            }
        } catch (Exception e) {
            log.error("远程调用失败,errorMsg={}", e.getMessage());
        } finally {
            client.close();
        }
        return result;

    }


    public static void main(String[] args) {

        try {
            String result = RestTemClient.doDelete("body","http://127.0.0.1:8080/tsec/slice/nssai");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
