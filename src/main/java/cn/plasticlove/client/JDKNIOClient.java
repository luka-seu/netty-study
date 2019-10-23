package cn.plasticlove.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author luka-seu
 * @description
 * @create 2019-10 23-21:58
 **/

public class JDKNIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socket = SocketChannel.open();
        socket.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress(8899);
        Selector selector = Selector.open();
        socket.connect(address);
        socket.register(selector, SelectionKey.OP_CONNECT);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true){
            try {
                selector.select();
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isConnectable()){
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        if (socketChannel.finishConnect()){
                            key.interestOps(SelectionKey.OP_READ);
                        }else{
                            System.out.println("connection error!");
                            break;
                        }
                    }else if (key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        buffer.clear();
                        while(socket.read(buffer)>0){
                            System.out.println(buffer.toString());
                            if (socket.read(buffer)<0){
                                break;
                            }
                        }
//                        System.out.println(socketChannel.getRemoteAddress());
                    }
                }catch (Exception e){
                    try {
                        key.cancel();
                        socket.close();
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            }
        }


    }
}
