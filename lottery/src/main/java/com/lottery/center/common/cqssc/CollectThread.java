package com.lottery.center.common.cqssc;

import java.util.Queue;

/**
 * 采集线程
 *  结果放入Queue
 */
public class CollectThread implements Runnable{

    private Queue queue;

    public CollectThread(){}

    public CollectThread(Queue queue){
        this.queue = queue;
    }

    @Override
    public void run() {




    }

}
