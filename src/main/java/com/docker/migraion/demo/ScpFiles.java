package com.docker.migraion.demo;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import lombok.AllArgsConstructor;

import java.io.IOException;

/**
 * @program: migration
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-10-15 09:51
 */
@AllArgsConstructor
public class ScpFiles {
    //"192.168.123.123"
    private String dataServerIp  ;
    //数据服务器的用户名= "root"
    private String dataServerUsername ;
    //数据服务器的密码= "1234"
    private String dataServerPassword ;
    //数据服务器的目的文件夹= "."
    //private String dataServerDestDir ;

    public void scpFile(String srcFile,String destFile){

        //从远程到本地的保存路径
        //    private String localDir = "D:\\上传文件的临时目录";

        //文件scp到数据服务器
        Connection conn = new Connection(dataServerIp);
        System.out.println("开始scp文件");
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(dataServerUsername, dataServerPassword);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
            SCPClient client = new SCPClient(conn);
            client.put(srcFile, destFile); //本地文件scp到远程目录
//            client.get(dataServerDestDir + "00审计.zip", localDir);//远程的文件scp到本地目录
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件scp到数据服务器时发生异常");
        }
        System.out.println("scp文件结束");
    }
}
