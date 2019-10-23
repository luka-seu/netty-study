package cn.plasticlove.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author luka-seu
 * @description
 * @create 2019-10 17-15:40
 **/

public class NIODemo1 {


    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");
        FileChannel in = inputStream.getChannel();
        FileChannel out = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while(true) {
            //必须得清除，不然会无休止的读取和写入
            buffer.clear();
            int read = in.read(buffer);
            if (-1==read){
                break;
            }
            //读写之间需要调用flip()方法完成反转
            buffer.flip();
            out.write(buffer);
        }
        inputStream.close();
        outputStream.close();
    }

}
