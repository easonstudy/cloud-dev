package com.cjcx.io.netty.basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class BasicSocketClient {
    public void start() {
        try {
            //获取通道
            SocketChannel channel = SocketChannel.open();
            //非阻塞
            channel.configureBlocking(false);

            //并没有实现连接，需要channel.finishedConnect()才算连接
            channel.connect(new InetSocketAddress("127.0.0.1", 9898));


            //获取选择器
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_CONNECT);

            //轮询选择器
            while (true) {
                //阻塞
                selector.select();
                //循环在选择器里的监听事件
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                if (it.hasNext()) {
                    SelectionKey sk = it.next();

                    if (sk.isConnectable()) {
                        System.out.println("client connectable");
                        //连接
                        channel.finishConnect();

                        //缓冲区
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        buf.put("客户端连接成功".getBytes());
                        buf.flip();
                        channel.write(buf);

                        //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
                        channel.register(selector, SelectionKey.OP_READ);
                    }

                    if (sk.isReadable()) {
                        System.out.println("client readable");
                        receive(sk);
                    }
                    if (sk.isWritable()) {
                        System.out.println("client writable");
                    }

                    //取消选择Key SelectionKey
                    it.remove();
                }
            }
        } catch (IOException e) {
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
            System.out.println("服务端消息:" + new String(buf.array(), 0, len));
            buf.clear();
        }

        /*//对消息进行响应
        String str = "服务端响应消息";
        buf.put(str.getBytes());
        buf.flip();
        schannel.write(buf);*/
    }


    public static void main(String[] args) {
        BasicSocketClient client = new BasicSocketClient();
        client.start();
    }
}
