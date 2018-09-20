package com.cjcx.io.netty.delimiter;

import com.corundumstudio.socketio.SocketIOChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * Netty 服务端启动流程
 * 第一步 : 创建两个线程池NioEventLoopGroup，负责接收事件和处理事件。
 * 第二步 : 设置启动类ServerBootstrap参数。
 * 第三步 : 端口绑定，并触发active事件，根据第二步的配置参数启动服务。
 * 第四步 : 关闭资源。
 * NioEventLoopGroup : 是用来处理I/O操作的多线程事件循环器。 Netty提供了许多不同的EventLoopGroup的实现用来处理不同传输协议。
 * ServerBootstrap : 启动NIO服务的辅助启动类。先配置Netty服务端启动参数，执行bind(PORT)方法才算真正启动服务。
 * group :  配置多线程事务轮询器
 * channel : 配置通道的类型。
 * handler : 服务器始化时就会执行的事件。
 * childHandler : 服务器在和客户端成功连接后会执行的事件。
 * initChannel : channelRegistered事件触发后执行，删除ChannelInitializer实例，添加该方法体中的handle。
 * option :      服务器始化的配置。
 * childOption : 服务器在和客户端成功连接后的配置。
 * bind(PORT) : 最关键的方法，绑定端口，启动服务，完成一些列配置。
 * sync() : 一直阻塞等待channel的停止。
 * <p>
 * SocketChannel : 继承了Channel，通过Channel可以对Socket进行各种操作。
 * ChannelHandler : 通过ChannelHandler来间接操纵Channel，简化了开发。
 * ChannelPipeline : 看成是一个ChandlerHandler的链表
 */
public class AdvanceSocketServer {

    private static final String DELIMITER = "_$";                // 拆包分隔符

    EventLoopGroup bossGroup = new NioEventLoopGroup(); //负责接收事件
    EventLoopGroup workerGroup = new NioEventLoopGroup(); //负责处理事件

    public void start() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //初始化时只执行
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //管道Pipe处理器:客 户端成功connect后才执行，这里实例化ChannelInitializer
                    .childHandler(new SocketIOChannelInitializer() {
                        @Override
                        protected void initChannel(Channel socketChannel) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer(DELIMITER.getBytes());        // 获取特殊分隔符的ByteBuffer
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(128, delimiter)); // 设置特殊分隔符用于拆包
//                          socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(8));       // 设置指定长度分割  不推荐，两者选其一
                            socketChannel.pipeline().addLast(new StringDecoder());                // 设置字符串形式的解码
                            socketChannel.pipeline().addLast(new AdvanceServerHandler());            // 自定义的服务器处理类，负责处理事件
                            socketChannel.pipeline().addLast(new AdvanceServerHandler2());            // 自定义的服务器处理类，负责处理事件
                        }
                    })
                    //服务器始化的配置, 设置缓冲区
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //服务器在和客户端成功连接后的配置, 设置保持连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            InetSocketAddress addr = new InetSocketAddress(9092);
            bootstrap.bind(addr).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AdvanceSocketServer server = new AdvanceSocketServer();
        server.start();
    }


}
