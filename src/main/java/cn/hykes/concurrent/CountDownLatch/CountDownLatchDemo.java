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
public class CountDownLatchDemo {

    private static int PLAYER_NUM = 10;


    /**
        10个选手比赛跑步，在枪响后同时起跑，全部到达终点后比赛结束：
     */

    public static void main(String[] args) {

        final CountDownLatch beginSignal = new CountDownLatch(1);
        final CountDownLatch endSignal = new CountDownLatch(PLAYER_NUM);

        ExecutorService executorService = Executors.newFixedThreadPool(PLAYER_NUM);

        for(int i=0;i<PLAYER_NUM;i++){
            final int num = i+1;
            Runnable runner = new Runnable(){

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    System.out.println("No. "+num+" is waiting...");
                    try {
                        beginSignal.await();
                        System.out.println("No. "+num+" begin running");
                        TimeUnit.SECONDS.sleep(new Random().nextInt(10));
                        System.out.println("No." + num + " arrived");
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }finally{
                        endSignal.countDown();
                    }
                }

            };
            executorService.execute(runner);
        }

        System.out.println("before Game Start");
        beginSignal.countDown();
        System.out.println("Game Start");
        System.out.println("---In the middle of the game---");
        try {
            endSignal.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            System.out.println("Game Over!");
            executorService.shutdown();
        }

    }

}