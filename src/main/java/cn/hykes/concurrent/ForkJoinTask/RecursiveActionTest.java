package cn.hykes.concurrent.ForkJoinTask;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/5
 */
public class RecursiveActionTest {


    /**
     Fork/Join 模式的使用方式非常直观。首先，我们需要编写一个 ForkJoinTask 来完成子任务的分割、中间结果的合并等工作。随后，我们将这个 ForkJoinTask 交给 ForkJoinPool 来完成应用的执行。
     通常我们并不直接继承 ForkJoinTask，它包含了太多的抽象方法。针对特定的问题，我们可以选择 ForkJoinTask 的不同子类来完成任务。RecursiveAction 是 ForkJoinTask 的一个子类，它代表了一类最简单的 ForkJoinTask：不需要返回值，当子任务都执行完毕之后，不需要进行中间结果的组合。如果我们从 RecursiveAction 开始继承，那么我们只需要重载 protected void compute() 方法。下面，我们来看看怎么为快速排序算法建立一个 ForkJoinTask 的子类：
     */

    public static class SortTask extends RecursiveAction {

        final long[] array;
        final int lo;
        final int hi;
        private int THRESHOLD = 20; //For demo only

        public SortTask(long[] array) {
            this.array = array;
            this.lo = 0;
            this.hi = array.length - 1;
        }

        public SortTask(long[] array, int lo, int hi) {
            this.array = array;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if (hi - lo < THRESHOLD) {
                sequentiallySort(array, lo, hi);
                System.out.println("array" + Arrays.toString(array));
            } else {
                int pivot = partition(array, lo, hi);
                System.out.println("pivot = " + pivot + ", low = " + lo + ", high = " + hi);
                System.out.println("array" + Arrays.toString(array));
                invokeAll(new SortTask(array, lo, pivot - 1), new SortTask(array, pivot + 1, hi));
            }
        }

        private int partition(long[] array, int lo, int hi) {
            long x = array[hi];
            int i = lo - 1;
            for (int j = lo; j < hi; j++) {
                if (array[j] <= x) {
                    i++;
                    swap(array, i, j);
                }
            }
            swap(array, i + 1, hi);
            return i + 1;
        }

        private void swap(long[] array, int i, int j) {
            if (i != j) {
                long temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        private void sequentiallySort(long[] array, int lo, int hi) {
            Arrays.sort(array, lo, hi + 1);//Only one question!
        }

    }

    public static void main(String... args) throws Exception {
        long[] array = new long[100];
        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = rand.nextLong() % 50; //For demo only
        }
        ForkJoinTask sort = new SortTask(array);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.execute(sort);
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(30, TimeUnit.SECONDS);
    }

    /**
     解释：
     SortTask 首先通过 partition() 方法将数组分成两个部分。
     随后，两个子任务将被生成并分别排序数组的两个部分。当子任务足够小时，再将其分割为更小的任务反而引起性能的降低。
     因此，这里我们使用一个 THRESHOLD，限定在子任务规模较小时，使用直接排序，而不是再将其分割成为更小的任务。
     RecursiveAction 提供的方法：
        invokeAll()：启动所有的任务，并在所有任务都正常结束后返回。如果其中一个任务出现异常，则其它所有的任务都取消。invokeAll() 的参数还可以是任务的数组。
        execute()：将 ForkJoinTask 类的对象提交给 ForkJoinPool，ForkJoinPool 将立刻开始执行 ForkJoinTask。
        shutdown()：执行此方法之后，ForkJoinPool 不再接受新的任务，但是已经提交的任务可以继续执行。如果希望立刻停止所有的任务，可以尝试 shutdownNow() 方法。
        awaitTermination()：阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束。
     */

}
