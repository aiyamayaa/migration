package com.docker.migraion.demo.hc.common;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @program: 5GSlicing
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-08-15 12:46
 */
public class RESTTrustManager implements TrustManager, X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
//        return new X509Certificate[0];
        return null;
    }

}
