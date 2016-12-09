package com.xdfs.common.util;

import com.xdfs.common.util.FileUtil;

import java.io.*;

/**
 * Created by xyy on 16-12-5.
 */
public class FIleTest {

    public static void main(String[] args)  {
      //.write("/home/xyy/temp/dump2","test");
        File source=new File("/home/xyy/temp/dump");
        File out =new File("/home/xyy/temp/dump2");

        try {
            FileUtil.read("/home/xyy/temp/dump33");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
//        System.out.println(source.length()+"  "+source.length()/2);
//        FileUtil.copyFile(source, new File("/home/xyy/temp/dump2"),source.length()/2,source.length()/2);
//
//        BufferedReader e = new BufferedReader(new FileReader(source.getAbsoluteFile()));
//        char[] bytes=new char[64*1024*1024];
//        e.read(bytes,0,(int)source.length());



    }



}
