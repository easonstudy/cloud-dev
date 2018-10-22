package com.lottery.center.common.cqssc;

import com.lottery.center.entity.CqsscResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;

/**
 * 展示(采集结果)线程
 *  (1s取一次)结果取自queue
 */
@Slf4j
public class DisplayThread implements Runnable{

    private Queue<CqsscResult> queue;

    public DisplayThread(){}

    public DisplayThread(Queue queue){
        this.queue = queue;
    }

    @Override
    public void run() {

        while (true){
            try {
                Thread.sleep(1000);
                CqsscResult result = queue.poll();
                if(result!=null) {
                    log.info(result.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
