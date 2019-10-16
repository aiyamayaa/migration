package com.docker.migraion.demo;


import com.docker.migraion.demo.hc.model.HttpReq;
import com.docker.migraion.demo.hc.utils.RESTClient;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: migration
 * @description: 迁移代理
 * @author: jiaxin_feng
 * @create: 2019-10-15 00:37
 * host1:10.112.42.78
 * host2:10.112.65.47
 */
@RestController
public class MigrationController {


    public volatile int flag = 0;

    public static String destHost = "http://10.112.65.47:8018/destHost";

    private static String mulu = "/var/lib/docker/containers/";


    @PostMapping(value = "/Migration")
    public String migration(@RequestBody String req ){
        System.out.println(req);
        //打印当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        System.out.println(req);

        String[] reqs = req.split("&");

        //创建检查点
        new Thread(()->{

            System.out.println("开始"+reqs[0]+" 检查点:"+simpleDateFormat.format(new Date())+"  ");
            ExecCl.exeCmd("docker checkpoint create "+reqs[0]+" ckp1");

            System.out.println("结束"+reqs[0]+" 检查点:"+simpleDateFormat.format(new Date())+"  ");

            flag = 1;
            System.out.println("flah=="+flag);
            if(reqs.length>3){
                System.out.println("开始"+reqs[3]+" Checkpoint:"+simpleDateFormat.format(new Date())+"  ");
                ExecCl.exeCmd("docker checkpoint create "+reqs[3]+" ckp1");
                System.out.println("结束"+reqs[3]+" Checkpoint End:"+simpleDateFormat.format(new Date())+"  ");
                flag=2;
            }

        }).start();
//        ScpFiles scpFiles = new ScpFiles("10.112.65.47","bni","bnibni123");


        //传输数据
        new Thread(()->{
            while (flag!=1){

            }
            //发送数据，之后通知host2开启服务
            System.out.println("开始传输检查点-1:"+simpleDateFormat.format(new Date()));

//            scpFiles.scpFile("/var/lib/docker/containers/"+reqs[1]+"/checkpoints/ckp1","/var/lib/docker/containers/"+reqs[2]+"/checkpoints");
            //sshpass -p bnibni123 scp -r Music/ bni@10.112.65.47:.
            ExecCl.exeCmd("sshpass -p bnibni123 scp -r "+mulu+reqs[1]+"/checkpoints/ckp1 bni@10.112.65.47:"+mulu+reqs[2]+"/checkpoints" );


            System.out.println("传输结束检查点-1"+simpleDateFormat.format(new Date()));
            flag= 0;
            sendMessage(destHost,reqs[0]+"&ckp1");

        }).start();
        new Thread(()->{

            if (req.length()>135){
                while (flag!=2){

                }
                System.out.println("开始传输检查点-2:"+simpleDateFormat.format(new Date()));
                ExecCl.exeCmd("sshpass -p bnibni123 scp -r "+mulu+reqs[4]+"/checkpoints/ckp1 bni@10.112.65.47:"+mulu+reqs[5]+"/checkpoints" );
                System.out.println("传输结束检查点-2"+simpleDateFormat.format(new Date()));
                sendMessage(destHost,reqs[3]+"&ckp1");
                flag= 0;
            }

        }).start();

        return req;
    }



    @PostMapping(value = "/destHost")
    public String migration2(@RequestBody String req ){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        //接收到的string为containerName&checkpointName
        String[] strings = req.split("&");
        System.out.println("开始"+strings[0]+" 检查点还原:"+simpleDateFormat.format(new Date())+"  ");
        ExecCl.exeCmd("docker start --checkpoint "+strings[1]+" "+strings[0]);

        System.out.println("结束"+strings[0]+" 检查点还原:"+simpleDateFormat.format(new Date())+"  ");

        return "ok";

    }






    private void sendMessage(String url ,String string) {
        //
   // public HttpReq(HttpMethod mthd, String url, String bdy, Map<String, String> hdr, Map<String, String> cki)
        RESTClient client = RESTClient.getInstance();
        Map<String,String> header = new HashMap<String, String>();
        header.putAll(new HashMap<String, String>(){{
            put("Accept","application/json");put("Content-Type","application/json");
            put("Accept-Encoding","gzip,default");}});
        HttpReq req = new HttpReq(HttpMethod.POST,url,string,header,null);
        client.exec(req);
    }

    public void sleep(){
        try {
            //睡眠1s
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MigrationController().sendMessage("http://10.112.65.47:8080/destHost","1&b");
    }

}
