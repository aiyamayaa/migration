package com.docker.migraion.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @program: migration
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-10-15 00:56
 */
public class Command {
    public static void main(String[] args) {
        String commandStr = "ping www.baidu.com";
        //String commandStr = "ipconfig";
        Command.exeCmd(commandStr);
    }
    public static void exeCmd(String commandStr) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);

            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
