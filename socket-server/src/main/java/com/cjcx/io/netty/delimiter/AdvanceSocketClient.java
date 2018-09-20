package com.cjcx.io.netty.delimiter;

import com.corundumstudio.socketio.SocketIOChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

public class AdvanceSocketClient {

    private static final String DELIMITER = "_$";                // 拆包分隔符

    public void start() {
        NioEventLoopGroup loop = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(loop)
                    .channel(NioSocketChannel.class)
                    //初始化时只执行
                    .handler(new SocketIOChannelInitializer() {
                        @Override
                        protected void initChannel(Channel socketChannel) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer(DELIMITER.getBytes());        // 获取特殊分隔符的ByteBuffer
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(128, delimiter)); // 设置特殊分隔符用于拆包
//                          socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(8));       // 设置指定长度分割  不推荐，两者选其一
                            socketChannel.pipeline().addLast(new StringDecoder());                // 设置字符串形式的解码
                            socketChannel.pipeline().addLast(new AdvanceClientHandler());            // 自定义的客户端处理类，负责处理事件
                        }
                    })
                    //初始化的配置, 设置缓冲区
                    .option(ChannelOption.SO_KEEPALIVE, true);

            InetSocketAddress addr = new InetSocketAddress(9092);
            ChannelFuture future = bootstrap.connect(addr).sync();
            //future.channel().writeAndFlush(Unpooled.copiedBuffer(("1+1" + DELIMITER).getBytes()));
            //future.channel().writeAndFlush(Unpooled.copiedBuffer(("6+1" + DELIMITER).getBytes()));
            future.channel().writeAndFlush(Unpooled.copiedBuffer(("2" + DELIMITER + "2" + DELIMITER).getBytes())); // 传的个数是9个，只打印了5个，还有4个在等待中
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        AdvanceSocketClient client = new AdvanceSocketClient();
        client.start();
    }
}
