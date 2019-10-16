package com.docker.migraion.demo.hc.utils;


import com.docker.migraion.demo.hc.common.RESTTrustManager;
import com.docker.migraion.demo.hc.constant.RESTConst;
import com.docker.migraion.demo.hc.model.Charsets;
import com.docker.migraion.demo.hc.model.HttpDeleteWithBody;
import com.docker.migraion.demo.hc.model.HttpReq;
import com.docker.migraion.demo.hc.model.HttpRsp;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @program: 5GSlicing
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-08-14 23:45
 */
@Log4j2
public final class RESTClient {
    // HTTP client
    private CloseableHttpClient hc = null;

    // HTTP client builder
    private HttpClientBuilder cb = null;

    // Request config
    private RequestConfig rc = null;

    // Instanceø
    private static RESTClient instance = null;



    public static RESTClient getInstance()
    {
        if (instance == null)
        {
            instance = new RESTClient();
        }
        return instance;
    }

    /**
     * （1）设置SSL/TLS
     * （2）设置连接超时和处理超时
     */
    private RESTClient()
    {
        SSLContext sc = null;
        try
        {
            sc = SSLContext.getInstance(RESTConst.TLS);
            TrustManager[] trustAllCrts = new TrustManager[] { new RESTTrustManager() };
            sc.init(null, trustAllCrts, null);
        }
        catch(Exception e)
        {
            log.error("Failed to initialize trust all certificates.", e);
        }

        HostnameVerifier hv = new HostnameVerifier()
        {
            public boolean verify(String arg0, SSLSession arg1)
            {
                return true;
            }
        };

        rc = RequestConfig.custom()
                .setConnectTimeout(RESTConst.TIME_OUT)
                .setConnectionRequestTimeout(RESTConst.TIME_OUT)
                .setSocketTimeout(RESTConst.TIME_OUT).build();

        this.cb = HttpClients.custom();
        this.cb.setSSLContext(sc);
        this.cb.setSSLHostnameVerifier(hv);

        hc = this.cb.build();
    }

    /**
     * 发送请求
     * @param req
     * @return
     */
    private HttpRsp exec(HttpRequestBase req)
    {
        CloseableHttpResponse hr = null;
        HttpRsp rsp = new HttpRsp();

        try
        {
            /* Send HTTP request ～～*/
            hr = hc.execute(req);
            HttpEntity he = hr.getEntity();
            if (null != he)
            {
                /* Receive HTTP response */
                String body = EntityUtils.toString(he, Charsets.UTF_8.getCname());
                if (null == body)
                {
                    body = StringUtils.EMPTY;
                }
                rsp.setBody(body);
            }
            else
            {
                log.warn("HTTP response is null.");
            }

            hr.setReasonPhrase("");
            rsp.setStatus(hr.getStatusLine().toString());
            rsp.setStatusCode(hr.getStatusLine().getStatusCode());
            rsp.setHeaders(new HashMap<String, String>());

            for (Header hdr : hr.getAllHeaders())
            {
                rsp.getHeaders().put(hdr.getName(), hdr.getValue());
            }
        }
        catch(Throwable e)
        {
            log.error("Http request failed.", e);
        }
        finally
        {
            HttpClientUtils.closeQuietly(hr);
        }

        return rsp;
    }


    /**
     * 发送请求@
     * @param req
     * @return
     */
    public HttpRsp exec(HttpReq req)
    {
        log.info("Start HTTP request: \r\n" + req);
        long time = System.currentTimeMillis();
        HttpRsp rsp = new HttpRsp();
        if (null == req)
        {
            rsp.setRawTxt("HTTP request argument is null. unable to process this HTTP request.");
            log.error(rsp.getRawTxt());
            return rsp;
        }

        if (null == req.getMethod())
        {
            rsp.setRawTxt("HTTP method is empty. unable to process this HTTP request.");
            log.error(rsp.getRawTxt());
            return rsp;
        }

        if (StringUtils.isEmpty(req.getUrl()))
        {
            rsp.setRawTxt("HTTP URL is empty. unable to process this HTTP request.");
            log.error(rsp.getRawTxt());
            return rsp;
        }

        try
        {

            /* Set HTTP method */
            HttpRequestBase hrb = null;
            if (HttpMethod.GET.equals(req.getMethod()))
            {
                hrb = new HttpGet(req.getUrl());
            }
            else if (HttpMethod.POST.equals(req.getMethod()))
            {
                hrb = new HttpPost(req.getUrl());
                ((HttpPost) hrb).setEntity(new StringEntity(req.getBody(), Charsets.UTF_8.getCname()));
            }
            else if (HttpMethod.PUT.equals(req.getMethod()))
            {
                hrb = new HttpPut(req.getUrl());
                ((HttpPut) hrb).setEntity(new StringEntity(req.getBody(), Charsets.UTF_8.getCname()));
            }
            /* added for DELETE with body*/
            else if (HttpMethod.DELETE.equals(req.getMethod()))
            {
              hrb = new HttpDeleteWithBody(req.getUrl());
                ((HttpDeleteWithBody) hrb).setEntity(req.getBody());
            }
//            else if(HttpMethod.PATCH.equals(req.getMethod())){
//                hrb = new HttpPatchWithBody(req.getUrl());
//                ((HttpPatchWithBody) hrb).setEntity(req.getBody());
//            }
            else if(HttpMethod.PATCH.equals(req.getMethod())){
                hrb = new HttpPatch(req.getUrl());
                ((HttpPatch) hrb).setEntity(new StringEntity(req.getBody(), Charsets.UTF_8.getCname()));
            }

            else
            {
                rsp.setRawTxt("Unsupported HTTP method: " + req.getMethod());
                log.error(rsp.getRawTxt());
                return rsp;
            }

            /* Set HTTP headers */
            if (MapUtils.isNotEmpty(req.getHeaders()))
            {
                Map<String, String> hdrs = req.getHeaders();
                Set<Entry<String, String>> es = hdrs.entrySet();
                for (Entry<String, String> e : es)
                {
                    hrb.setHeader(e.getKey(), e.getValue());
                }
            }

            /* Set HTTP cookies */
            if (MapUtils.isNotEmpty(req.getCookies()))
            {
                StringBuilder sb = new StringBuilder();
                Map<String, String> cks = req.getCookies();
                Set<Entry<String, String>> es = cks.entrySet();
                for (Entry<String, String> e : es)
                {
                    sb.append("; ")
                            .append(e.getKey())
                            .append("=")
                            .append(e.getValue());
                }
                String hdrVal = sb.toString().replaceFirst("; ", "");
                hrb.setHeader(RESTConst.COOKIE, hdrVal);
                req.getHeaders().put(RESTConst.COOKIE, hdrVal);
            }

            /* Execute HTTP request */
            hrb.setConfig(rc);
            rsp = this.exec(hrb);
            rsp.setRawTxt(req.toRawTxt() + rsp.toRawTxt());
        }
        catch(Throwable e)
        {
            rsp.setRawTxt("Failed to process this HTTP request: \r\n" + req + "\r\nResponse messages from server: \r\n" + e.getMessage());
            log.error("Failed to process this HTTP request: \r\n" + req, e);
        }

        rsp.setTime(System.currentTimeMillis() - time);
        log.info("Done HTTP request: \r\n" + rsp);
        return rsp;
    }

    /**
     * close
     */
    public void close()
    {
        if (null == this.hc)
        {
            return;
        }

        try
        {
            this.hc.close();
            hc = this.cb.build();
        }
        catch(IOException e)
        {
            log.error("Failed to close connection.", e);
        }
    }
}
