package cn.hykes.concurrent.BlockingQueue.Delayed;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;

/**
 * Desc:
 * Mail: hehaiyangwork@qq.com
 * Date: 2017/12/2
 */
public class Exam {

    /**
     模拟一个考试的日子，考试时间为120分钟，30分钟后才可交卷，当时间到了，或学生都交完卷了考试结束。
     这个场景中几个点需要注意：
     考试时间为120分钟，30分钟后才可交卷，初始化考生完成试卷时间最小应为30分钟
     对于能够在120分钟内交卷的考生，如何实现这些考生交卷
     对于120分钟内没有完成考试的考生，在120分钟考试时间到后需要让他们强制交卷
     在所有的考生都交完卷后，需要将控制线程关闭
     实现思想：用DelayQueue存储考生（Student类），每一个考生都有自己的名字和完成试卷的时间，
        Teacher线程对DelayQueue进行监控，收取完成试卷小于120分钟的学生的试卷。当考试时间120分钟到时，
        先关闭Teacher线程，然后强制DelayQueue中还存在的考生交卷。每一个考生交卷都会进行一次countDownLatch.countDown()，
        当countDownLatch.await()不再阻塞说明所有考生都交完卷了，而后结束考试。

     */

    /**
     其他业务场景：
        淘宝订单业务:下单之后如果三十分钟之内没有付款就自动取消订单。
            当订单定时取消需要修改数据库订单状态，但是怎么确定订单什么时候应该改变状态，解决方案有下面两种：
                第一种，写个定时器去每分钟扫描数据库，这样更新及时，但是如果数据库数据量大的话，会对数据库造成很大的压力。
                第二种，创建订单的时候，把这条记录保存到DelayQueue队列里面，并且用一个子线程不断地轮训这个出队的订单。然后进行订单状态修改的状态。
                系统启动时，应该把数据库里面未付款的订单加载到Queue里面，并且调用线程池生成子线程，这样可以确保子线程出异常后，不会影响到后续的订单处理。启动子线程不断监控要出队的订单。
                客户取消订单的时候，需要程序从Queue里面删除订单信息。
        饿了吗订餐通知:下单成功后60s之后给用户发送短信通知。
     */

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        int studentNumber = 10;
        CountDownLatch countDownLatch = new CountDownLatch(studentNumber+1);
        DelayQueue<Student> students = new DelayQueue();
        Random random = new Random();
        for (int i = 0; i < studentNumber; i++) {
            students.put(new Student("student"+(i+1), 30+random.nextInt(120),countDownLatch));
        }
        Thread teacherThread =new Thread(new Teacher(students));
        students.put(new EndExam(students, 120,countDownLatch,teacherThread));
        teacherThread.start();
        countDownLatch.await();
        System.out.println(" 考试时间到，全部交卷！");
    }

}
