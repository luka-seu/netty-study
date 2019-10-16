package cn.plasticlove.netty.lifecycle.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author luka-seu
 **/

public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        //client只需要一个事件组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //服务端：ServerBootstrap;  客户端：bootstrap
            Bootstrap bootstrap = new Bootstrap();
            //客户端用handler即可
            bootstrap.group(workerGroup).channel(NioSocketChannel.class).handler(new MyClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8898).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
