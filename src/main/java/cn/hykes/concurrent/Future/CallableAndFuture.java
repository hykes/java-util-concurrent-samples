package cn.hykes.concurrent.Future;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/5
 */
public class CallableAndFuture {

    /**
      Callable接口类似于Runnable，从名字就可以看出来了，
     但是Runnable不会返回结果，并且无法抛出返回结果的异常，而Callable功能更强大一些，
     被线程执行后，可以返回值，这个返回值可以被Future拿到，也就是说，Future可以拿到异步执行任务的返回值，
     */

    /**
     Callable接口可以看作是Runnable接口的补充，call方法带有返回值，并且可以抛出异常。

     Future的核心思想是：一个方法f，计算过程可能非常耗时，等待f返回，显然不明智。可以在调用f的时候，立马返回一个Future，可以通过Future这个数据结构去控制方法f的计算过程。

     这里的控制包括：
     get方法：获取计算结果（如果还没计算完，也是必须等待的）
     cancel方法：还没计算完，可以取消计算过程
     isDone方法：判断是否计算完
     isCancelled方法：判断计算是否被取消
     */

    public static void main(String[] args) {
//        Callable<Integer> callable = new Callable<Integer>() {
//            public Integer call() throws Exception {
//                return new Random().nextInt(100);
//            }
//        };

        /**
         FutureTask实现了两个接口，Runnable和Future，所以它既可以作为Runnable被线程执行，
         又可以作为Future得到Callable的返回值，那么这个组合的使用有什么好处呢？假设有一个很耗时的返回值需要计算，
         并且这个返回值不是立刻需要的话，那么就可以使用这个组合，用另一个线程去计算返回值，
         而当前线程在使用这个返回值之前可以做其它的操作，等到需要这个返回值时，再通过Future得到
         */
//        Callable<Integer> callable = () -> new Random().nextInt(100);
//        FutureTask<Integer> future = new FutureTask(callable);
//        new Thread(future).start();

        /**
         * 通过ExecutorService的submit方法执行Callable，并返回Future
         */
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        Future<Integer> future = threadPool.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                return new Random().nextInt(100);
            }
        });

        try {
            Thread.sleep(500);// 可能做一些事情
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
