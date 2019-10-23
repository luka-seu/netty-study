package cn.plasticlove.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author luka-seu
 * @description
 * @create 2019-10 23-21:37
 **/

public class JDKNIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //很关键，配置非阻塞
        serverSocketChannel.configureBlocking(false);
        //获取ServerSocket
        ServerSocket serverSocket = serverSocketChannel.socket();
        //绑定端口
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocket.bind(address);
        //打开selector用于管理channels
        Selector selector = Selector.open();
        //注册为接收连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer buffer = ByteBuffer.wrap("HI! NETTY!".getBytes());
        while (true){
            //等待需要处理的新事件，阻塞直到下一个事件传入
            try {
                selector.select();
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
            //获取所有的SelectionKey
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                try {
                    //检查事件是否是一个就绪且可以被接受的连接
                    if (selectionKey.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector,SelectionKey.OP_WRITE|SelectionKey.OP_READ,buffer.duplicate());
                        System.out.println("connection from: "+client);
                    }
                    if (selectionKey.isWritable()){
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        ByteBuffer msg = (ByteBuffer) selectionKey.attachment();
                        while (msg.hasRemaining()){
                            if (client.write(msg)==0){
                                break;
                            }
                        }
                        client.close();
                    }
                }catch (Exception e){
                    selectionKey.cancel();
                    try {
                        selectionKey.channel().close();
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }


                }
            }

        }
    }
}
