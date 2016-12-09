package com.xdfs.common.util;

import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by xyy on 16-12-5.
 */
public class FileUtil {

    public static String read(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
            BufferedReader e = new BufferedReader(new FileReader((new File(fileName)).getAbsoluteFile()));
            String s;
            try {
                while ((s = e.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                e.close();
            }

        return sb.toString();
    }

    public static void write(String fileName, String text) throws IOException {
            PrintWriter e = new PrintWriter((new File(fileName)).getAbsoluteFile());

            try {
                e.print(text);
            } finally {
                e.close();
            }

    }

    public static void copyFile(File source, File dest, Long startSize, Long endSize) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(dest);
            FileChannel out3 = new FileOutputStream("/home/xyy/temp/dump3").getChannel();
            inputChannel = fis.getChannel();
            outputChannel = fos.getChannel();

            // System.out.println(inputChannel.position()+" "+inputChannel.size());
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size() / 2);
            System.out.println(inputChannel.position() + "  " + inputChannel.size());
            out3.transferFrom(inputChannel, 0, inputChannel.size());
            System.out.println(inputChannel.position() + "  " + inputChannel.size());
        } finally {
            fis.close();
            fos.close();
            inputChannel.close();
            outputChannel.close();
        }
    }

    public static void copyFile(String source, String dest, Long startSize, Long endSize) throws IOException {
        copyFile(new File(source), new File(dest), startSize, endSize);
    }

    public static int read(String fileName, byte[] buf) throws IOException {

        InputStream input = null;
        try {
            input = new FileInputStream(fileName);
            return input.read(buf);
        } finally {
            input.close();
        }
    }

    public static boolean write(String fileName, byte[] bytes) throws IOException {
        OutputStream output = null;
        try {
            File file = new File(fileName);
            File fileParent = file.getParentFile();
            if (fileParent!=null&&!fileParent.exists()){
                fileParent.mkdirs();
            }
            output = new FileOutputStream(file);
            output.write(bytes);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            output.close();
        }
        return false;
    }

    private static void copyFileUsingFileStreams(File source, File dest) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            // while buffer读满了从头覆盖buf继续读
            while ((bytesRead = input.read(buf)) > 0) {
                // System.out.println(bytesRead);
                output.write(buf, 0, bytesRead);
            }

            // System.out.println(bytesRead);
        } finally {
            input.close();
            output.close();
        }
    }

}