package com.docker.migraion.demo.hc.model;

import com.docker.migraion.demo.hc.constant.RESTConst;
import com.docker.migraion.demo.hc.utils.RESTUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @program: 5GSlicing
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-08-15 00:02
 */
@Data
@Log4j2
public class HttpRsp implements Serializable {

//    ContentType？？？？？？

    /**
     * response raw text
     */
    @JsonProperty("raw_text")
    private String rawTxt;

    /**
     * response body
     */
    private String body;

    /**
     * Headers
     */
    private Map<String, String> headers;

    /**
     * Cookies
     */
    @JsonIgnore
    private Map<String, String> cookies;

    /**
     * Date
     */
    private String date;

    /**
     * Time
     */
    private Long time;

    /**
     * Http status
     */
    private String status;

    /**
     * HTTP status code
     */
    private Integer statusCode;

    public HttpRsp()
    {
        this.date = RESTUtil.nowDate();
        this.time = 0L;
    }

    public HttpRsp(HttpRsp rsp)
    {
        if (null == rsp)
        {
            return;
        }

        if (null != rsp.getHeaders())
        {
            this.headers = new LinkedHashMap<String, String>(rsp.getHeaders());
        }
        if (null != rsp.getCookies())
        {
            this.cookies = new LinkedHashMap<String, String>(rsp.getCookies());
        }

        this.rawTxt = rsp.getRawTxt();
        this.body = rsp.getBody();
        this.date = rsp.getDate();
        this.time = rsp.getTime();
        this.status = rsp.getStatus();
        this.statusCode = rsp.getStatusCode();
    }

    public String toRawTxt()
    {
        // Status
        StringBuilder sb = new StringBuilder();
        sb.append(RESTConst.RSP_TAG)
                .append(RESTUtil.lines(2))
                .append(this.status)
                .append(RESTUtil.lines(2));

        // Headers
        if (MapUtils.isNotEmpty(this.headers))
        {
            sb.append(RESTConst.HDR_TAG).append(RESTUtil.lines(1));
            Map<String, String> hdr = this.headers;
            Set<Entry<String, String>> es = hdr.entrySet();
            for (Entry<String, String> e : es)
            {
                sb.append(e.toString().replaceFirst("=", " : "))
                        .append(RESTUtil.lines(1));
            }
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

        return sb.toString();
    }

    @Override
    public String toString()
    {

        StringBuilder builder = new StringBuilder();
        builder.append("Http Response [rawTxt=");
        builder.append(rawTxt);
        builder.append(", body=");
        builder.append(body);
        builder.append(", headers=");
        builder.append(headers);
        builder.append(", cookies=");
        builder.append(cookies);
        builder.append(", date=");
        builder.append(date);
        builder.append(", time=");
        builder.append(time);
        builder.append(", status=");
        builder.append(status);
        builder.append(", statusCode=");
        builder.append(statusCode);
        builder.append("]");
        return builder.toString();
    }

}
