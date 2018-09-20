package com.cjcx.io.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * ByteBuf
 * 堆缓存区
 * 非堆缓存区
 * 复合缓冲区
 */
public class TestBuf {

    @Test
    public void test4() {
        //复合缓冲区
        ByteBuf buf = Unpooled.compositeBuffer();
        System.out.println(buf);
    }


    @Test
    public void test3() {
        //非堆缓存区
        ByteBuf buf = Unpooled.directBuffer(1024);
        buf.writeBytes("1".getBytes());
        buf.writeBytes("你".getBytes());

        int len = buf.readableBytes();
        System.out.println("len:" + len);
        byte[] bytes = new byte[len];
        System.out.println(new String(buf.readBytes(bytes).array()));
    }


    @Test
    public void test2() {
        //堆缓存区
        ByteBuf buf = Unpooled.buffer(1024);
        buf.writeBytes("1".getBytes());
        buf.writeBytes("你".getBytes());

        int len = buf.readableBytes();
        System.out.println("len:" + len);
        byte[] bytes = new byte[len];
        System.out.println(new String(buf.readBytes(bytes).array()));
    }

    @Test
    public void test1() {
        ByteBuf buf = Unpooled.buffer();
        System.out.println(buf);
    }
}
