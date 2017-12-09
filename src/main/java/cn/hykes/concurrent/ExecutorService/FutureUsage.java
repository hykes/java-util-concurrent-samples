package cn.hykes.concurrent.ExecutorService;

import java.util.concurrent.*;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/5
 */
public class FutureUsage {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Object> task = new Callable<Object>() {
            public Object call() throws Exception {

                Thread.sleep(4000);

                Object result = "finished";
                return result;
            }
        };

        Future<Object> future = executor.submit(task);
        System.out.println("task submitted");

        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }

        // Thread won't be destroyed.
    }
}
