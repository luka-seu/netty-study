package cn.plasticlove.client;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author luka-seu
 * @description
 * @create 2019-10 23-21:19
 **/

public class JDKOIOClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8899);

        try {
            OutputStream out = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            dataOutputStream.writeUTF("HI! THis is client!");
            InputStream in = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(in);
            System.out.println("from server: " + dataInputStream.readUTF());
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
