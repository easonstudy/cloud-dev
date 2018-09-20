package com.lottery.center.common.cqssc;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ColletMain {

    //结果
    Map<String, CqsscResult> map = new HashMap<>();

    //采集中结果队列
    Queue queue = new ArrayBlockingQueue(1024);

    public static void main(String[] args) {

    }
}
