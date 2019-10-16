package com.docker.migraion.demo;

/**
 * @program: migration
 * @description:
 * @author: jiaxin_feng
 * @create: 2019-10-15 13:19
 */
public class ExecCl {

    public static void exeCmd(String string){

        try {
            String command = string;
            Process p = Runtime.getRuntime().exec(command);
            StreamCaptureThread errorStream = new StreamCaptureThread(p.getErrorStream());
            StreamCaptureThread outputStream = new StreamCaptureThread(p.getInputStream());
            new Thread(errorStream).start();
            new Thread(outputStream).start();
            p.waitFor();

            String result = command + "\n" + outputStream.output.toString()
                    + errorStream.output.toString();
            System.out.print(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
