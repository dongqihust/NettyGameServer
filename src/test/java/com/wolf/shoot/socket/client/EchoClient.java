package com.wolf.shoot.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * netty 客户端模拟
 *
 * @author mingge
 */
public class EchoClient {


    public static void main(String[] args) throws Exception {
        new EchoClient().connect("127.0.0.1", 9999);
    }

    public void connect(String addr, int port) throws Exception {
        final EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
//                            ch.pipeline().addLast(new StringDecoder());
//                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new EchoClientHandler());
//                            ch.pipeline().addLast(new EchoClientHandler2());
                        }
                    });
            ChannelFuture f = b.connect(addr, port).sync();
            System.out.println("连接服务器:" + f.channel().remoteAddress() + ",本地地址:" + f.channel().localAddress());
            f.channel().closeFuture().sync();//等待客户端关闭连接
//            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    group.shutdownGracefully();
//                }
//            }));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            group.shutdownGracefully();
        }
    }
}
