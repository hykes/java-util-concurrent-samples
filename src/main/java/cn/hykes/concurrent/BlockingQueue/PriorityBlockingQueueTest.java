package cn.hykes.concurrent.BlockingQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/3
 */
public class PriorityBlockingQueueTest {

    public static class Item implements Comparable<Item>{

        private String name;

        private Integer priority;

        private Item(){}

        public Item(String name, Integer priority){
            this.name = name;
            this.priority = priority;
        }

        @Override
        public int compareTo(Item o) {
            if(o == this) return 0;
            if (this.priority > o.priority) {
                return 1;
            }else if (this.priority == o.priority) {
                return 0;
            }else {
                return -1;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BlockingQueue<Item> priorityBlockingQueue = new PriorityBlockingQueue();

        priorityBlockingQueue.put(new Item("1", 1));
        priorityBlockingQueue.put(new Item("2", 10));
        priorityBlockingQueue.put(new Item("3", 5));

        do{
            Item item = priorityBlockingQueue.poll();
            if (item != null) {
                System.out.println(String.format("this is item:(%s)", item.name));
            }
        }while (priorityBlockingQueue.size()!= 0);

    }

}
