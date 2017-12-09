package cn.hykes.concurrent.CountDownLatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/6
 */
public class CountDownLatchTest {

    public static class Student implements Runnable {

        private int num;
        private CountDownLatch cdlatch;

        Student(int num,CountDownLatch latch){
            this.num = num;
            this.cdlatch = latch;
        }

        @Override
        public void run() {
            doExam();
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Student "+num+" finished!");
            cdlatch.countDown();
        }

        private void doExam(){
            System.out.println("Student "+num+" is doing the exam!");
        }

    }

    public static class Teacher implements Runnable{

        private CountDownLatch cdlatch;

        public Teacher(CountDownLatch latch){
            this.cdlatch = latch;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                System.out.println("teacher is waiting...");
                cdlatch.await();
                System.out.println("teacher is collecting......");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     简单来说，CountDownLatch是一个同步的辅助类，允许一个或多个线程一直等待，直到其它线程完成它们的操作。

     这里就涉及两个问题：

     1.如何让一个或多个线程一直等待；

     2.如何让这些线程知道其它线程已经完成它们的操作

     这两个问题主要是使用一个count的属性解决。使用count初始化CountDownLatch，然后需要等待的线程调用await方法。await方法会一直受阻塞直到count=0。

     而其它线程完成自己的操作后，调用countDown()使计数器count减1。当count减到0时，所有在等待的线程均会被释放，并且count无法被重置。
     */

    public static void main(String[] args) throws Exception{

        ExecutorService executor = Executors.newCachedThreadPool();

        CountDownLatch latch = new CountDownLatch(3);

        Student s1 = new Student(101, latch);
        Student s2 = new Student(102, latch);
        Student s3 = new Student(103, latch);
        Teacher t = new Teacher(latch);

        executor.execute(t);
        executor.execute(s1);
        executor.execute(s2);
        executor.execute(s3);

        executor.shutdown();

    }

}
