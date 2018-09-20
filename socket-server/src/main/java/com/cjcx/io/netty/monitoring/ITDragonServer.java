package com.cjcx.io.netty.monitoring;


import com.cjcx.io.netty.marshalling.ITDragonMarshallerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

public class ITDragonServer {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(ITDragonMarshallerFactory.buildMarshallingDecoder());
                            socketChannel.pipeline().addLast(ITDragonMarshallerFactory.builMarshallingEncoder());
                            socketChannel.pipeline().addLast(new ITDragonServerHandler());
                            //ocketChannel.pipeline().addLast(new ReadTimeoutHandler(5));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(Integer.parseInt(ITDragonCoreParam.PORT.getValue()))
                    .addListener(new FutureListener<Void>() {
                        @Override
                        public void operationComplete(Future future) throws Exception {
                            if (future.isSuccess()) {
                                System.out.println("sok");
                            } else {
                                System.out.println("error");
                            }
                        }
                    })
                    .sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
