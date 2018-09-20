package com.cjcx.pay.obj;


import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 对象池
 */
public class ObjectPool {

    private int min = 10;

    private int max = 50;

    private Vector objects = null;

    public synchronized void createPool() {
        if (objects != null) {
            return;
        }

        objects = new Vector();
        for (int i = 0; i < min; i++) {
            objects.addElement(new PooledObject(new Obj()));
        }
    }


    /**
     * 获取池对象
     *
     * @return
     */
    public synchronized Object getObject() {
        Object conn = getFreeObject();

        while (conn == null) {
            wait(250);
            conn = getFreeObject();
        }
        return conn;
    }


    private Object getFreeObject() {
        Object obj = findFreeObject();

        if (obj == null) {
            synchronized (this) {
                int cnt = getBusyCount();
                if (cnt == objects.size() && objects.size() < max) {
                    objects.addElement(new PooledObject(new Obj()));
                    obj = findFreeObject();
                }
            }
        }
        return obj;
    }


    /**
     * 遍历Vector池，获取可用对象
     *
     * @return
     */
    private Object findFreeObject() {
        Object obj = null;
        PooledObject pooledObject = null;

        Enumeration enumeration = objects.elements();
        while (enumeration.hasMoreElements()) {
            pooledObject = (PooledObject) enumeration.nextElement();

            if (!pooledObject.isBusy()) {
                obj = pooledObject.getObject();
                pooledObject.setBusy(true);
                break;
            }
        }
        return obj;
    }

    private int getBusyCount() {
        int cnt = 0;
        PooledObject pooledObject = null;
        Enumeration enumeration = objects.elements();
        while (enumeration.hasMoreElements()) {
            pooledObject = (PooledObject) enumeration.nextElement();

            if (pooledObject.isBusy()) {
                cnt++;
            }
        }
        return cnt;
    }


    /**
     * 此函数返回一个对象到对象池中，并把此对象置为空闲。
     * 所有使用对象池获得的对象均应在不使用此对象时返回它。
     */

    public void returnObject(Object obj) {

        // 确保对象池存在，如果对象没有创建（不存在），直接返回
        if (objects == null) {
            return;
        }

        PooledObject pObj = null;
        Enumeration enumerate = objects.elements();

        // 遍历对象池中的所有对象，找到这个要返回的对象对象
        while (enumerate.hasMoreElements()) {
            pObj = (PooledObject) enumerate.nextElement();

            // 先找到对象池中的要返回的对象对象
            if (obj == pObj.getObject()) {
                // 找到了 , 设置此对象为空闲状态
                pObj.setBusy(false);
                break;
            }
        }
    }

    /**
     * 使程序等待给定的毫秒数
     */
    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
        }
    }


    /**
     * 关闭对象池中所有的对象，并清空对象池。
     */
    public synchronized void closeObjectPool() {
        // 确保对象池存在，如果不存在，返回
        if (objects == null) {
            return;
        }

        PooledObject pObj = null;
        Enumeration enumerate = objects.elements();
        while (enumerate.hasMoreElements()) {
            pObj = (PooledObject) enumerate.nextElement();
            // 如果忙，等 5 秒
            if (pObj.isBusy()) {
                wait(5000); // 等 5 秒
            }
            // 从对象池向量中删除它
            objects.removeElement(pObj);
        }
        // 置对象池为空
        objects = null;
    }


    public Vector getObjects() {
        return objects;
    }

    public static void main(String[] args) {
        ObjectPool pool = new ObjectPool();
        System.out.println("创建对象池");
        pool.createPool();

        //创建固定线程池 为面延迟回收对象
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 100; i++) {
            System.out.println(" 对象池 对象数量: " + pool.getObjects().size());

            Obj pObj = (Obj) pool.getObject();
            pObj.syaHello(i);

            //延迟20ms 回收对象
            FutureTask<Void> future =
                    new FutureTask<Void>(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            Thread.sleep(20);
                            pool.returnObject(pObj);
                            return null;
                        }
                    });
            executor.execute(future);
        }

        System.out.println("清空对象池");
        pool.closeObjectPool();

        executor.shutdown();
    }


    /**
     * 池对象
     */
    class PooledObject {
        Object object = null;
        boolean busy = false;

        public PooledObject() {
        }

        public PooledObject(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public boolean isBusy() {
            return busy;
        }

        public void setBusy(boolean busy) {
            this.busy = busy;
        }
    }


    /**
     * 对象 待处理对象
     */
    class Obj {

        public void syaHello(int i) {
            System.out.println(this.toString() + " ==> hello :" + i);
            ;
        }
    }

}
