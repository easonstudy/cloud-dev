package com.cjcx.wechat.open.aa;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadDemo {
    public static void main(String[] args) {
        try {
            ReentrantLock reentrantLock=new ReentrantLock();
            Condition conditionA=reentrantLock.newCondition();
            Condition conditionB=reentrantLock.newCondition();
            Condition conditionC=reentrantLock.newCondition();
            MyThread myThreadA=new MyThread("A",conditionA,conditionB,reentrantLock);
            MyThread myThreadB=new MyThread("B",conditionB,conditionC,reentrantLock);
            MyThread myThreadC=new MyThread("C",conditionC,null,reentrantLock);
            myThreadA.start();
            myThreadB.start();
            myThreadC.start();
            Thread.sleep(100);
            reentrantLock.lock();
            conditionA.signal();
            reentrantLock.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    static class MyThread extends Thread {
       private  String ThreadID;
        private Condition conditionNow,conditionNext;
        private ReentrantLock reentrantLock;

        public MyThread(String threadID, Condition conditionNow, Condition conditionNext, ReentrantLock reentrantLock) {
            this.ThreadID = threadID;
            this.conditionNow = conditionNow;
            this.conditionNext = conditionNext;
            this.reentrantLock = reentrantLock;
        }

        @Override
        public void run() {
            reentrantLock.lock();
            try {
                try {
                    conditionNow.await();
                    for (int i = 0; i <1 ; i++) {
                        System.out.println(this.ThreadID);
                    }
                    if(conditionNext!=null){
                        conditionNext.signal();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }finally {
                reentrantLock.unlock();
            }

        }

    }



}
