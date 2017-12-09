package cn.hykes.concurrent.ForkJoinTask;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/4
 */
public class RecursiveTaskTest {

    /**
     ForkJoinTask是jdk1.7整合Fork/Join，即拆分fork+合并join，性能上有大大提升。
     思想:充分利用多核CPU把计算拆分成多个子任务，并行计算，提高CPU利用率大大减少运算时间。有点像，MapReduce思路感觉大致一样。
     jdk7中已经提供了最简洁的接口，让你不需要太多时间关心并行时线程的通信，死锁问题，线程同步，下面是它提供的接口：
     RecursiveAction 无返回值任务。
     RecursiveTask有返回值类型。

     */

    /**
     * 计算某数到某数的和，返回结果"和"
     */
    private static class Demo extends RecursiveTask<Integer> {
        private int start;
        private int end;

        private Demo(){}

        public Demo(int start, int end) {
            this.start = start;
            this.end = end;
        }

        //计算
        @Override
        protected Integer compute() {
            int sum = 0;
            if (start - end < 100) {
                for (int i = start; i < end; i++) {
                    sum += i;
                }
            } else {//间隔有100则拆分多个任务计算
                int middle = (start + end) / 2;
                Demo left = new Demo(start, middle);
                Demo right = new Demo(middle + 1, end);
                left.fork();
                right.fork();

                sum = left.join() + right.join();
            }
            return sum;
        }
    }

    public static void main(String[] args) throws Exception {
        Demo d = new Demo(1,101);
        /**
         * ForkJoinPool提供了一系列的submit方法，计算任务。
         * ForkJoinPool默认的线程数通过Runtime.availableProcessors()获得，
         * 因为在计算密集型的任务中，获得多于处理性核心数的线程并不能获得更多性能提升，
         * 该方法也可以传以前的Runnable, Callback的接口实现（底层会将其封装成ForkJoinTask对象）
         */
        ForkJoinPool forkJoinPool = new ForkJoinPool();//对线程池的扩展
        Future<Integer> result = forkJoinPool.submit(d);
        System.out.println(result.get());
    }

}
