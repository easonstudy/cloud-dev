package com.cjcx.io.netty.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Netty基础
 */
public class BasicSocketServer {

    /**
     * 启动
     */
    public void start() {
        try {
            //通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            //通道 非阻塞
            ssc.configureBlocking(false);
            //通道 绑定端口
            ssc.bind(new InetSocketAddress(9898));
            //获取选择器
            Selector selector = Selector.open();

            //注册
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            //轮询选择器
            while (selector.select() > 0) {
                //循环在选择器里的监听事件
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                if (it.hasNext()) {
                    SelectionKey sk = it.next();
                    if (sk.isConnectable()) {
                        System.out.println("server connectable");
                    }
                    if (sk.isAcceptable()) {
                        System.out.println("server accept");
                        //接收就绪 事件OK  //获取 的客户端
                        SocketChannel schannel = ssc.accept();
                        //客户端 切换为非阻塞
                        schannel.configureBlocking(false);
                        schannel.register(selector, SelectionKey.OP_READ);
                    }
                    if (sk.isReadable()) {
                        System.out.println("server readable");
                        receive(sk);
                    }
                    if (sk.isWritable()) {
                        System.out.println("server writable");
                        send(sk);
                    }

                    //取消选择Key SelectionKey
                    it.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收数据处理
     */
    public void receive(SelectionKey sk) throws IOException {
        //读就绪 事件OK
        SocketChannel schannel = (SocketChannel) sk.channel();

        ByteBuffer buf = ByteBuffer.allocate(1024);
        int len = 0;
        while ((len = schannel.read(buf)) > 0) {
            buf.flip();
            System.out.println("客户端消息: " + new String(buf.array(), 0, len));
            buf.clear();
        }

        //对消息进行响应
        String str = "服务端响应消息, 我来自服务端";
        buf.put(str.getBytes());
        buf.flip();
        schannel.write(buf);
    }

    /**
     * 发送数据处理
     */
    public void send(SelectionKey sk) throws IOException {
        String sendContent = "服务端发送消息: ss";

        SocketChannel schannel = (SocketChannel) sk.channel();

        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(sendContent.getBytes());
        buf.flip();
        System.out.println(sendContent);
        schannel.write(buf);
    }


    /**
     * 服务端
     */
    public static void main(String[] args) {
        BasicSocketServer server = new BasicSocketServer();

        server.start();

    }
}

