package cn.hykes.concurrent.Future;

import java.util.concurrent.*;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/5
 */
public class BigCallableAndFutureTest {

    /**
        其实也可以不使用CompletionService，提交到CompletionService中的Future是按照完成的顺序排列的
        可以先创建一个装Future类型的集合，用Executor提交的任务返回值添加到集合中，最后遍历集合取出数据，这种做法中Future是按照添加的顺序排列的。
        考虑如下场景：多线程下载，结果用Future返回。第一个文件特别大，后面的文件很小。
            1. 用CompletionService，能很快知道已经下载完文件的结果(不是第一个)；
            2. 用集合，必须等第一个文件下载结束后，才会获得其他文件的下载结果。
     */

    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        CompletionService<Integer> cs = new ExecutorCompletionService(threadPool);
        for(int i = 1; i < 5; i++) {
            final int taskID = i;
//            cs.submit(new Callable<Integer>() {
//                public Integer call() throws Exception {
//                    return taskID;
//                }
//            });
            cs.submit(() -> taskID);

        }
        // 可能做一些事情
        for(int i = 1; i < 5; i++) {
            try {
                System.out.println(cs.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
