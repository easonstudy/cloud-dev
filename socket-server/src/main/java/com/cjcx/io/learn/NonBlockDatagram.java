package com.cjcx.io.learn;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * UDP
 * DatagramChannel
 *
 * 根据IP:PORT直接发送
 * send()     发送数据
 * receive()  接收数据 并返回SocketAddress客户端地址,可以回消息
 */
public class NonBlockDatagram {

    /**
     * 客户端
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            //获取通道
            DatagramChannel dc = DatagramChannel.open();
            //非阻塞
            dc.configureBlocking(false);

            //分配缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);

            //配置接收消息线程
            new Thread(new datagramMessageThread(dc)).start();

            //待发送内容
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String udpsend = scanner.next();
                //写入缓冲区
                buf.put(udpsend.getBytes());
                buf.flip();

                //发送数据 不需要建立连接, 根据IP:PORT直接发送
                dc.send(buf, new InetSocketAddress("127.0.0.1", 9898));
                buf.clear();
            }

            dc.close();
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
            DatagramChannel dc = DatagramChannel.open();
            //通道 非阻塞
            dc.configureBlocking(false);
            //通道 绑定端口
            dc.bind(new InetSocketAddress(9898));

            //获取选择器
            Selector selector = Selector.open();

            //注册
            dc.register(selector, SelectionKey.OP_READ);

            //轮询选择器
            while (selector.select() > 0) {
                //循环在选择器里的监听事件
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                if (it.hasNext()) {
                    SelectionKey sk = it.next();

                    if (sk.isReadable()) {
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        //
                        SocketAddress socketAddress = dc.receive(buf);
                        buf.flip();
                        System.out.println("server got:" + new String(buf.array(), 0, buf.limit()));
                        buf.clear();

                        //响应消息回客户端
                        String response = "server got message: " + (int)(Math.random() * 1000);
                        buf.put(response.getBytes());
                        buf.flip();
                        dc.send(buf, socketAddress);
                        buf.clear();
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

class datagramMessageThread implements Runnable {
    private DatagramChannel dc;

    public datagramMessageThread(DatagramChannel dc) {
        this.dc = dc;
    }

    @Override
    public void run() {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        SocketAddress socketAddress = null;
        while (true){
            try {
                socketAddress = dc.receive(buf);
                if(socketAddress != null) {
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, buf.limit()));
                    buf.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
