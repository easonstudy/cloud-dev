package com.cjcx.io.learn;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/*
 * 一、使用 NIO 完成网络通信的三个核心：
 *
 * 1. 通道（Channel）：负责连接
 *
 * 	   java.nio.channels.Channel 接口：
 * 			|--SelectableChannel
 * 				|--SocketChannel
 * 				|--ServerSocketChannel
 * 				|--DatagramChannel
 *
 * 				|--Pipe.SinkChannel
 * 				|--Pipe.SourceChannel
 *
 * 2. 缓冲区（Buffer）：负责数据的存取
 *
 * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 *
 */

/**
 * TCP
 * SocketChannel
 * ServerSocketChannel
 */
public class NonBlock {

    private ByteBuffer buf;

    /**
     * 客户端
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            //获取通道
            SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
            //非阻塞
            channel.configureBlocking(false);

            //分配缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);

            //待发送内容
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String send = scanner.next();
                //写入缓冲区
                buf.put(send.getBytes());
                //改为读模式，读取数据
                buf.flip();
                channel.write(buf);
                buf.clear();
            }

            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 服务端
     */
    @Test
    public void server() {
        try {
            //通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            //通道 非阻塞
            ssc.configureBlocking(false);
            //通道 绑定端口
            ssc.bind(new InetSocketAddress(9898));

            //获取选择器
            //Selector一起使用时，Channel必须处于非阻塞模式下。
            Selector selector = Selector.open();

            //注册
            //SelectionKey.OP_CONNECT;  连接就绪
            //SelectionKey.OP_ACCEPT;   接收就绪
            //SelectionKey.OP_READ,;    读就绪
            //SelectionKey.OP_WRITE;    写就绪
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            //轮询选择器
            while (selector.select() > 0) {
                //循环在选择器里的监听事件
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                if (it.hasNext()) {
                    SelectionKey sk = it.next();

                    if (sk.isAcceptable()) {
                        //接收就绪 事件OK  //获取 的客户端
                        SocketChannel schannel = ssc.accept();
                        //客户端 切换为非阻塞
                        schannel.configureBlocking(false);

                        schannel.register(selector, SelectionKey.OP_READ);
                    } else if (sk.isReadable()) {
                        //读就绪 事件OK
                        SocketChannel schannel = (SocketChannel) sk.channel();

                        buf = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = schannel.read(buf)) > 0) {
                            buf.flip();
                            System.out.println(new String(buf.array(), 0, len));
                            buf.clear();
                        }
                        schannel.shutdownInput();

                    }
                    //取消选择Key SelectionKey
                    it.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

