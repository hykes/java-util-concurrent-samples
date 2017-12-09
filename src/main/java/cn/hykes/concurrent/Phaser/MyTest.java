package cn.hykes.concurrent.Phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/6
 */
public class MyTest {

    /**
     Phaser是一个灵活的线程同步工具，他包含了CyclicBarrier和CountDownLatch的相关功能
     首先，来看一下如何用Phaser替代CountDownLatch。对于CountDownLatch而言，有2个重要的方法，
     一个是await()方法，可以使线程进入等待状态，在Phaser中，与之对应的方法是awaitAdvance(int n)。
     CountDownLatch中另一个重要的方法是countDown()，使计数器减一，当计数器为0时所有等待的线程开始执行，
     在Phaser中，与之对应的方法是arrive()。下面的例子创建了3个线程，打印一些字母，但是线程创建好后并不立刻执行，
     而是在主程序中对其进行控制，3秒钟后所有进程同时开始执行，一下是使用Phaser实现的版本，在注释中解释了如何改造成CountDownLatch版本。
     */
    public static void main(String[] args) throws Exception {
        Phaser phaser = new Phaser(1); //此处可使用CountDownLatch(1)
        for(int i=0; i<3; i++) {
            new MyThread((char)(97+i), phaser).start();
        }
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        phaser.arrive();  //此处可使用latch.countDown()
    }

    public static class MyThread extends Thread {
        private char c;
        private Phaser phaser;

        public MyThread(char c, Phaser phaser) {
            this.c = c;
            this.phaser = phaser;
        }

        @Override
        public void run() {
            phaser.awaitAdvance(phaser.getPhase()); //此处可使用latch.await()
            for(int i=0; i<100; i++) {
                System.out.print(c+" ");
                if(i % 10 == 9) {
                    System.out.println();
                }
            }
        }
    }
}
