package cn.hykes.concurrent.ThreadFactory;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/5
 */
public class ThreadFactoryTest{

    public static class WorkThread extends Thread {

        private Runnable target;
        private AtomicInteger counter;

        public WorkThread(Runnable target, AtomicInteger counter) {
            this.target = target;
            this.counter = counter;
        }

        @Override
        public void run() {
            try {
                target.run();
            } finally {
                int c = counter.getAndDecrement();
                System.out.println("terminate no." + c + " Threads");
            }
        }
    }

    public static class MyThread implements Runnable {

        @Override
        public void run() {
            System.out.println("complete a task!!!");
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService =  Executors.newCachedThreadPool(new ThreadFactory() {
            private AtomicInteger count = new AtomicInteger();
            public Thread newThread(Runnable r) {
                int c = count.incrementAndGet();
                System.out.println("create no." + c + " Threads");
                return new WorkThread(r,count);

            }
        });

        executorService.execute(new MyThread());
        executorService.execute(new MyThread());
        executorService.execute(new MyThread());
        executorService.execute(new MyThread());
        executorService.execute(new MyThread());
        executorService.execute(new MyThread());

        executorService.shutdown();

        try {
            executorService.awaitTermination(1200, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}