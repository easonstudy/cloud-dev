package com.cjcx.io.learn;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * <p> A pipe consists of a pair of channels:
 * 使用SinkChannel 写数据
 * A writable {@link Pipe.SinkChannel sink} channel
 * 使用SourceChannel 读数据
 * a readable {@link Pipe.SourceChannel source}
 */
public class PipeDemo {

    public static void main(String[] args) throws IOException {

        //1. 获取管道
        Pipe pipe = Pipe.open();

        //2.管道 写数据
        WritePileThread wrunable = new WritePileThread(pipe);
        Thread w = new Thread(wrunable);
        w.start();

        //3.管道读数据
        Thread r = new Thread(new ReadPileThread(pipe));
        r.start();

        //sourceChannel.close();
        //sinkChannel.close();
    }


}

class WritePileThread implements Runnable {
    private Pipe pipe;

    private volatile boolean end = false;

    public WritePileThread(Pipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public void run() {
        ByteBuffer wbuf = ByteBuffer.allocate(1024);
        //将缓冲区中的数据写入管道
        Pipe.SinkChannel sinkChannel = pipe.sink();
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                wbuf.clear();
                wbuf.put("通过单向管道发送数据".getBytes());
                wbuf.flip();
                sinkChannel.write(wbuf);
            }

            end = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getEnd() {
        return end;
    }
}

class ReadPileThread implements Runnable {
    private Pipe pipe;

    private volatile boolean end = false;

    public ReadPileThread(Pipe pipe) {
        this.pipe = pipe;
    }

    @Override
    public void run() {
        ByteBuffer rbuf = ByteBuffer.allocate(1024);
        //读取缓冲区中的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        try {
            int len = 0;
            while (true) {
                if ((len = sourceChannel.read(rbuf)) > 0) {
                    rbuf.flip();
                    System.out.println(new String(rbuf.array(), 0, len));
                    rbuf.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}