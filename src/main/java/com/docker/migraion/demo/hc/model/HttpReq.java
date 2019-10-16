package com.docker.migraion.demo.hc.model;

import com.docker.migraion.demo.hc.constant.RESTConst;
import com.docker.migraion.demo.hc.utils.RESTUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @program: 5GSlicing
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-08-15 00:23
 */
@Data
@Log4j2
public class HttpReq implements Serializable {
    /**
     * URL
     */
    private String url;

    /**
     * HTTP method
     */
    private HttpMethod method;

    /**
     * request raw text
     */
    @JsonIgnore
    private String rawTxt;

    /**
     * request body
     */
    private String body;

    /**
     * Date
     */
    private String date;

    /**
     * Headers
     */
    private Map<String, String> headers;

    /**
     * Cookies
     */
    private Map<String, String> cookies;
    public HttpReq()
    {
        this.date = RESTUtil.nowDate();
    }

    public HttpReq(HttpMethod mthd, String url, String bdy, Map<String, String> hdr, Map<String, String> cki)
    {
        this.url = url;
        this.method = mthd;
        this.body = bdy;
        this.headers = hdr;
        this.cookies = cki;
        this.date = RESTUtil.nowDate();
    }

    public HttpReq(HttpReq req)
    {
        if (null == req)
        {
            return;
        }

        if (null != req.getHeaders())
        {
            this.headers = new LinkedHashMap<String, String>(req.getHeaders());
        }
        if (null != req.getCookies())
        {
            this.cookies = new LinkedHashMap<String, String>(req.getCookies());
        }

        this.url = req.getUrl();
        this.rawTxt = req.getRawTxt();
        this.method = req.getMethod();
        this.body = req.getBody();
        this.date = req.getDate();
    }

    /**
     *
     * @return
     */
    public String toRawTxt()
    {
        // Method and URL
        StringBuilder sb = new StringBuilder();
        sb.append(RESTConst.REQ_TAG)
                .append(RESTUtil.lines(2))
                .append(this.getMethod())
                .append(" ")
                .append(this.getUrl())
                .append(RESTUtil.lines(2));

        // Headers
        sb.append(RESTConst.HDR_TAG)
                .append(RESTUtil.lines(1));
        Map<String, String> hdr = this.getHeaders();
        Set<Entry<String, String>> es = hdr.entrySet();
        for (Entry<String, String> e : es)
        {
            sb.append(e.toString().replaceFirst("=", " : "))
                    .append(RESTUtil.lines(1));
        }

        // Body
        if (StringUtils.isNotBlank(this.body))
        {
            sb.append(RESTUtil.lines(1))
                    .append(RESTConst.BDY_TAG)
                    .append(RESTUtil.lines(1))
                    .append(this.body)
                    .append(RESTUtil.lines(1));
        }

        sb.append(RESTUtil.lines(1));
        return sb.toString();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Http Request [url=");
        builder.append(url);
        builder.append(", method=");
        builder.append(method);
        builder.append(", rawTxt=");
        builder.append(rawTxt);
        builder.append(", body=");
        builder.append(body);
        builder.append(", date=");
        builder.append(date);
        builder.append(", headers=");
        builder.append(headers);
        builder.append(", cookies=");
        builder.append(cookies);
        builder.append("]");
        return builder.toString();
    }
}
