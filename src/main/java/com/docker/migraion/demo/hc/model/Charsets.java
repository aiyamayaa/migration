package com.docker.migraion.demo.hc.model;

/**
 * @program: 5GSlicing
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-08-15 00:38
 */

public enum Charsets {
    UTF_8(0, "UTF-8"),
    US_ASCII(1, "US-ASCII"),
    ISO_8859_1(2, "ISO-8859-1"),
    UTF_16(3, "UTF-16"),
    UTF_16LE(4, "UTF-16LE"),
    UTF_16BE(5, "UTF-16BE");

    private int cid;

    private String cname;
    private Charsets(int cid, String cname)
    {
        this.cid = cid;
        this.cname = cname;
    }

    public int getCid()
    {
        return cid;
    }

    public void setCid(int cid)
    {
        this.cid = cid;
    }

    public String getCname()
    {
        return cname;
    }

    public void setCname(String cname)
    {
        this.cname = cname;
    }

}
