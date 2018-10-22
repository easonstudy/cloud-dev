package com.lottery.center.common.cqssc;

import com.lottery.center.entity.CqsscResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;

/**
 * 采集线程
 *  结果放入Queue
 */
@Slf4j
public class CollectThread implements Runnable{

    private Queue<CqsscResult> queue;

    public CollectThread(){}

    public CollectThread(Queue queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while (true) {
                Thread.sleep(3000);
                i++;
                queue.offer(new CqsscResult(i, i+""));
                log.info(Thread.currentThread().getName()+" put " + i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
