package com.docker.migraion.demo.hc.utils;


import com.docker.migraion.demo.hc.model.HttpDeleteWithBody;
import com.docker.migraion.demo.hc.model.HttpPatchWithBody;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @program: 5GSlicing
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-08-19 15:07
 */
@Log4j2
public class MyRestClient {

    public static String doDelete(String url, Map<String,String> header, String body){

        String result = null;
        CloseableHttpClient httpClient = null;
        HttpDeleteWithBody deleteWithBody = null;

        try {

            httpClient = HttpClients.createDefault();
            deleteWithBody = new HttpDeleteWithBody(url);
            if(header!=null){
                for(String key:header.keySet()){
                    deleteWithBody.setHeader(key,header.get(key));
                }
            }
            deleteWithBody.setEntity(new StringEntity(body));
            CloseableHttpResponse response  = httpClient.execute(deleteWithBody);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);

            if (200 == response.getStatusLine().getStatusCode()) {
                log.debug("远程调用成功.msg={}", result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String doPatch(String url, Map<String,String> header,String body){
        HttpPatchWithBody patchWithBody = null;
        CloseableHttpClient client = null;
        String result = null;

        patchWithBody = new HttpPatchWithBody(url);
        client = HttpClients.createDefault();
        patchWithBody.setEntity(body);
        patchWithBody.setHeader(header);
        try {
            CloseableHttpResponse response = client.execute(patchWithBody);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);

            if (200 == response.getStatusLine().getStatusCode()) {
                log.debug("远程调用成功.msg={}", result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}

