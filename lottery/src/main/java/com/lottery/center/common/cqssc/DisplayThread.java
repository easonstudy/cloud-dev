package com.lottery.center.common.cqssc;

import java.util.Queue;

/**
 * 展示(采集结果)线程
 *  (1s取一次)结果取自queue
 */
public class DisplayThread implements Runnable{

    private Queue queue;

    public DisplayThread(){}

    public DisplayThread(Queue queue){
        this.queue = queue;
    }

    @Override
    public void run() {




    }
}
