package cn.hykes.concurrent.BlockingDeque;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/3
 */
public class LinkedBlockingDequeTest {

    public static void main(String[] args) throws Exception{
        BlockingDeque<Integer> blockingDeque = new LinkedBlockingDeque(10);

        blockingDeque.addFirst(1);

        blockingDeque.addLast(2);

        System.out.println(blockingDeque.takeLast());

        System.out.println(blockingDeque.takeFirst());
    }
}
