package cn.plasticlove.server;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author luka-seu
 * @description
 * @create 2019-10 23-21:06
 **/

public class JDKOIOServer {

    public static void main(String[] args) throws IOException {
        final ServerSocket server = new ServerSocket(8899);
        try {
            while(true){
                //没有连接的话会一直阻塞
                final Socket clientSocket = server.accept();
                System.out.println("connection from: "+clientSocket);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try {
                            //获取向客户端写数据的socket通道
                            out = clientSocket.getOutputStream();
                            //封装一层输出流，不用直接处理字节数据
                            DataOutputStream outputStream = new DataOutputStream(out);
                            outputStream.writeUTF("HI! NETTY!");
                            //刷新缓冲区
                            out.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
