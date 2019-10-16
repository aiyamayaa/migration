package com.docker.migraion.demo.hc.utils;

import com.docker.migraion.demo.hc.constant.RESTConst;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @program: 5GSlicing
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-08-15 00:08
 */
@Log4j2
public class RESTUtil {

    /**
     * instance to json Text
     * @param instance
     * @param <T>
     * @return
     */
    public static <T> String tojsonText(T instance)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance);
        }
        catch(Exception e)
        {
            log.error("Write object [" + instance + "] as json string failed.", e);
        }

        return StringUtils.EMPTY;
    }


    /**
     * return nowDate
     * @return
     */
    public static String nowDate()
    {
        SimpleDateFormat fmat = new SimpleDateFormat(RESTConst.DATE_FORMAT);
        return fmat.format(new Date());
    }

    /**
     * lines
     * @param num
     * @return
     */
    public static String lines(int num)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++)
        {
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
