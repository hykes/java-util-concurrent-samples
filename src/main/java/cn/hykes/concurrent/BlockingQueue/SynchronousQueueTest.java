package cn.hykes.concurrent.BlockingQueue;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/3
 */
public class SynchronousQueueTest {

    public static void main(String[] args) throws Exception {
        // 如果为 true，则等待线程以 FIFO 的顺序竞争访问；否则顺序是未指定的。
        // SynchronousQueue<Integer> sc =new SynchronousQueue<>(true);
        SynchronousQueue<Integer> sc = new SynchronousQueue<>(); // 默认不指定的话是false，不公平的
        new Thread(() -> { //生产者线程，使用的是lambda写法，需要使用JDK1.8
            while (true) {
                try {
                    sc.put(new Random().nextInt(50));//没有线程等待获取元素的话，阻塞在此处等待一直有线程获取元素时候放到队列继续向下运行
//                    sc.offer(2);// 没有线程等待获取元素的话，不阻塞在此处，如果该元素已添加到此队列，则返回 true；否则返回 false
//                    sc.offer(2, 5, TimeUnit.SECONDS);// 没有线程等待获取元素的话，阻塞在此处等待指定时间，如果该元素已添加到此队列，则返回true；否则返回 false
                    System.out.println("添加操作运行完毕...");//是操作完毕，并不是添加或获取元素成功!
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {//消费者线程。使用的是lambda创建的线程写法需要使用jdk1.8
            while (true) {
                try {
                    System.out.println("-----------------> sc.take: " + sc.take());
                    System.out.println("-----------------> 获取操作运行完毕...");//是操作完毕，并不是添加或获取元素成功!
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
