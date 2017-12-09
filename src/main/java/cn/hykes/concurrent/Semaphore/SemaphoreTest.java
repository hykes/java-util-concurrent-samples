package cn.hykes.concurrent.Semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/6
 */
public class SemaphoreTest {

    /**
     一个计数信号量。从概念上讲，信号量维护了一个许可集。
     如有必要，在许可可用前会阻塞每一个 acquire()，然后再获取该许可。每个 release() 添加一个许可，
     从而可能释放一个正在阻塞的获取者。但是，不使用实际的许可对象，Semaphore 只对可用许可的号码进行计数，
     并采取相应的行动。拿到信号量的线程可以进入代码，否则就等待。通过acquire()和release()获取和释放访问许可。
     */

    public static void main(String[] args) {
        // 线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        // 只能5个线程同时访问
        final Semaphore semp = new Semaphore(5);
        // 模拟20个客户端访问
        for (int index = 0; index < 20; index++) {
            final int NO = index;
            Runnable run = new Runnable() {
                public void run() {
                    try {
                        // 获取许可
                        /**
                         从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断。获取一个许可（如果提供了一个）并立即返回，将可用的许可数减 1。
                         如果没有可用的许可，则在发生以下两种情况之一前，禁止将当前线程用于线程安排目的并使其处于休眠状态：
                         某些其他线程调用此信号量的 release() 方法，并且当前线程是下一个要被分配许可的线程；或者
                         其他某些线程中断当前线程。
                         如果当前线程：
                         被此方法将其已中断状态设置为 on ；或者
                         在等待许可时被中断。
                         则抛出 InterruptedException，并且清除当前线程的已中断状态。
                         抛出：
                         InterruptedException - 如果当前线程被中断
                         */
                        semp.acquire();
                        System.out.println("Accessing: " + NO);
                        Thread.sleep((long) (Math.random() * 10000));
                        // 访问完后，释放 ，如果屏蔽下面的语句，则在控制台只能打印5条记录，之后线程一直阻塞

                        /**
                         释放一个许可，将其返回给信号量。释放一个许可，将可用的许可数增加 1。如果任意线程试图获取许可，则选中一个线程并将刚刚释放的许可给予它。然后针对线程安排目的启用（或再启用）该线程。
                         不要求释放许可的线程必须通过调用 acquire() 来获取许可。通过应用程序中的编程约定来建立信号量的正确用法。
                         */
                        semp.release();

                    } catch (InterruptedException e) {
                    }
                }
            };
            exec.execute(run);
        }
        // 退出线程池
        exec.shutdown();
    }

}
