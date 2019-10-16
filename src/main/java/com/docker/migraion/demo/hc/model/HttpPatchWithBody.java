package com.docker.migraion.demo.hc.model;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

/**
 * @program: 5GSlicing
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-08-19 15:39
 */
public class HttpPatchWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "PATCH";
    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpPatchWithBody(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpPatchWithBody(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpPatchWithBody() {
        super();
    }


    public void setEntity(String body) {

        try {
            super.setEntity(new StringEntity(body));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void setHeader(Map<String,String> header){
        if(header!=null){
            for(String key:header.keySet()){
                super.setHeader(key,header.get(key));
            }
        }
    }
}
