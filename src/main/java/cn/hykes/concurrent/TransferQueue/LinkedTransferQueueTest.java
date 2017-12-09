package cn.hykes.concurrent.TransferQueue;

import java.util.Random;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/3
 */
public class LinkedTransferQueueTest {

    public static class Producer implements Runnable {
        private final TransferQueue<String> queue;

        public Producer(TransferQueue<String> queue) {
            this.queue = queue;
        }

        private String produce() {
            Integer i = new Random().nextInt(100);
            System.out.println("Producer:" + i);
            return "your lucky number " + i;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (queue.hasWaitingConsumer()) {
                        queue.transfer(produce());
                    }
                    TimeUnit.SECONDS.sleep(1);//生产者睡眠一秒钟,这样可以看出程序的执行过程
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public static class Consumer implements Runnable {
        private final TransferQueue<String> queue;

        public Consumer(TransferQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                System.out.println(" Consumer " + Thread.currentThread().getName()
                        + queue.take());
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) throws Exception {
        TransferQueue<String> queue = new LinkedTransferQueue();
        Thread producer = new Thread(new Producer(queue));
        producer.setDaemon(true); //设置为守护进程使得线程执行结束后程序自动结束运行
        producer.start();
        for (int i = 0; i < 10; i++) {
            Thread consumer = new Thread(new Consumer(queue));
            consumer.setDaemon(true);
            consumer.start();
            try {
                // 消费者进程休眠一秒钟，以便以便生产者获得CPU，从而生产产品
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
