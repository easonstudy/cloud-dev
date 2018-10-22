package com.lottery.center.common.cqssc;

import com.lottery.center.entity.CqsscResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ColletMain {

    //结果
    private Map<String, CqsscResult> map = new HashMap<>();

    //采集中结果队列
    private static Queue<CqsscResult> queue = new ArrayBlockingQueue(1024);

    public static void main(String[] args) {

        //CollectManager manager = new CollectManager(queue);
        //manager.bootstarp();

        for (int i = 0; i < 3; i++) {
            CollectThread threadA = new CollectThread(queue);
            new Thread(threadA, "Collect Thread" + i).start();
        }

        DisplayThread displayThread = new DisplayThread(queue);
        new Thread(displayThread, "Display Thread").start();
    }
}
