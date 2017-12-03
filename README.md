# JDK java.util.concurrent包学习

java.util.concurrent包的类都来自于JSR-166：Concurrent Utilities，官方的描述叫做“The JSR proposes a set of medium-level utilities that provide functionality commonly needed in concurrent programs. ”。

这里参考的concurrent包来自JDK8，简要地记录类的功能和使用方式。

[JSR-166](https://www.jcp.org/en/jsr/detail?id=166)
[API手册](http://gee.cs.oswego.edu/dl/jsr166/dist/docs/overview-summary.html)

## 并发容器

这些容器的关键方法大部分都实现了线程安全的功能，却不使用同步关键字(synchronized)。

### Queue.class，队列接口

值得注意的是Queue接口本身定义的几个常用方法的区别：

1. add方法和offer方法的区别在于超出容量限制时前者抛出异常，后者返回false；
2. remove方法和poll方法都从队列中拿掉元素并返回，但是他们的区别在于空队列下操作前者抛出异常，而后者返回null；
3. element方法和peek方法都返回队列顶端的元素，但是不把元素从队列中删掉，区别在于前者在空队列的时候抛出异常，后者返回null。

## 阻塞队列

### BlockingQueue.class，阻塞队列接口

![原理](/images/BlockingQueue.png)

BlockingQueue提供了线程安全的队列访问方式：

当阻塞队列进行插入数据时，如果队列已满，线程将会阻塞等待直到队列非满；从阻塞队列取数据时，如果队列已空，线程将会阻塞等待直到队列非空。并发包下很多高级同步类的实现都是基于BlockingQueue实现的。

BlockingQueue 具有 4 组不同的方法用于插入、移除以及对队列中的元素进行检查。如果请求的操作不能得到立即执行的话，每个方法的表现也不同。这些方法如下：

|  -  |   抛异常    | 特定值  | 阻塞 |  超时  |
| --- |:----------:| -----:|-----:|-----:|
| 插入 | add(o)     | offer(o) | pull(o) | offer(o, timeout, timeunit) |
| 移除 | remove(o)  | poll(o) | take(o) | poll(timeout, timeunit) |
| 检查 | element(o) | peek(o) | - | - |

四组不同的行为方式解释：

1. 抛异常：如果试图的操作无法立即执行，抛一个异常。
2. 特定值：如果试图的操作无法立即执行，返回一个特定的值(常常是 true / false)。
3. 阻塞：如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行。
4. 超时：如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行，但等待时间不会超过给定值。返回一个特定值以告知该操作是否成功(典型的是true / false)。

无法向一个 BlockingQueue 中插入 null。如果你试图插入 null，BlockingQueue 将会抛出一个 NullPointerException。

可以访问到 BlockingQueue 中的所有元素，而不仅仅是开始和结束的元素。比如说，你将一个对象放入队列之中以等待处理，但你的应用想要将其取消掉。那么你可以调用诸如 remove(o) 方法来将队列之中的特定对象进行移除。但是这么干效率并不高(译者注：基于队列的数据结构，获取除开始或结束位置的其他对象的效率不会太高)，因此你尽量不要用这一类的方法，除非你确实不得不那么做。

#### ArrayBlockingQueue.class，阻塞队列，数组实现

ArrayBlockingQueue 是一个有界的阻塞队列，其内部实现是将对象放到一个数组里。有界也就意味着，它不能够存储无限多数量的元素。它有一个同一时间能够存储元素数量的上限。你可以在对其初始化的时候设定这个上限，但之后就无法对这个上限进行修改了(译者注：因为它是基于数组实现的，也就具有数组的特性：一旦初始化，大小就无法修改)。

[example](https://github.com/hykes/java-util-concurrent-samples/blob/047b8e14d923b645befa61da6b91f4d0f9aa5b2e/src/main/java/cn/hykes/concurrent/BlockingQueue/ArrayBlockingQueueTest.java)

#### LinkedBlockingQueue.class，阻塞队列，链表实现

LinkedBlockingQueue 内部以一个链式结构(链接节点)对其元素进行存储。如果需要的话，这一链式结构可以选择一个上限。如果没有定义上限，将使用 Integer.MAX_VALUE 作为上限。

[example](https://github.com/hykes/java-util-concurrent-samples/blob/047b8e14d923b645befa61da6b91f4d0f9aa5b2e/src/main/java/cn/hykes/concurrent/BlockingQueue/LinkedBlockingQueueTest.java)

#### DelayQueue.class，阻塞队列，并且元素是Delay的子类，保证元素在达到一定时间后才可以取得到

DelayQueue是一个无界的BlockingQueue，用于放置实现了 java.util.concurrent.Delayed 接口的对象，其中的对象只能在其到期时才能从队列中取走。这种队列是有序的，即队头对象的延迟到期时间最长。注意：不能将null元素放置到这种队列中。

[example](https://github.com/hykes/java-util-concurrent-samples/blob/047b8e14d923b645befa61da6b91f4d0f9aa5b2e/src/main/java/cn/hykes/concurrent/BlockingQueue/DelayQueueTest.java)

#### PriorityBlockingQueue.class，优先级阻塞队列

PriorityBlockingQueue 是一个无界的并发队列。它使用了和类 java.util.PriorityQueue 一样的排序规则。你无法向这个队列中插入 null 值。所有插入到 PriorityBlockingQueue 的元素必须实现 java.lang.Comparable 接口。因此该队列中元素的排序就取决于你自己的 Comparable 实现。

[example](https://github.com/hykes/java-util-concurrent-samples/blob/c6161af5f644707783f9e2ba24d8f58801231359/src/main/java/cn/hykes/concurrent/BlockingQueue/PriorityBlockingQueueTest.java)

#### SynchronousQueue.class，同步队列，但是队列长度为0，生产者放入队列的操作会被阻塞，直到消费者过来取，所以这个队列根本不需要空间存放元素；有点像一个独木桥，一次只能一人通过，还不能在桥上停留

SynchronousQueue 是一个特殊的队列，它的内部同时只能够容纳单个元素。如果该队列已有一元素的话，试图向队列中插入一个新元素的线程将会阻塞，直到另一个线程将该元素从队列中抽走。同样，如果该队列为空，试图向队列中抽取一个元素的线程将会阻塞，直到另一个线程向队列中插入了一条新的元素。据此，把这个类称作一个队列显然是夸大其词了。它更多像是一个汇合点。

[example](https://github.com/hykes/java-util-concurrent-samples/blob/c6161af5f644707783f9e2ba24d8f58801231359/src/main/java/cn/hykes/concurrent/BlockingQueue/SynchronousQueueTest.java)

#### 阻塞队列原理

其实阻塞队列实现阻塞同步的方式很简单，使用的就是是lock锁的多条件（condition）阻塞控制。使用BlockingQueue封装了根据条件阻塞线程的过程，而我们就不用关心繁琐的await/signal操作了。

### BlockingDeque.class，双端阻塞队列接口

![原理](/images/BlockingDeque.png)

一个线程可以插入元素到队列的任一端。如果队列full,那么线程将会阻塞，直到其他线程从队列中取出一个元素为止。如果队列empty,那么从队列中取元素的线程将会阻塞，直到其他线程插入一个元素为止。

|  -  |   抛异常    | 特定值  | 阻塞 |  超时  |
| --- |:----------:| -----:|-----:|-----:|
| 插入 | addFirst(o)     | offerFirst(o) | pullFirst(o) | offerFirst(o, timeout, timeunit) |
| 移除 | removeFirst(o)  | pollFirst(o) | takeFirst(o) | pollFirst(timeout, timeunit) |
| 检查 | getFirst(o) | peekFirst(o) | - | - |
| 插入 | addLast(o)     | offerLast(o) | pullLast(o) | offerLast(o, timeout, timeunit) |
| 移除 | removeLast(o)  | pollLast(o) | takeLast(o) | pollLast(timeout, timeunit) |
| 检查 | getLast(o) | peekLast(o) | - | - |

#### LinkedBlockingDeque.class，阻塞双端队列，链表实现

[example]([example](https://github.com/hykes/java-util-concurrent-samples/blob/047b8e14d923b645befa61da6b91f4d0f9aa5b2e/src/main/java/cn/hykes/concurrent/BlockingDeque/LinkedBlockingDequeTest.java))

## 非阻塞队列：

### ConcurrentLinkedDeque.class，非阻塞双端队列，链表实现

### ConcurrentLinkedQueue.class，非阻塞队列，链表实现

## 转移队列：

### TransferQueue.class，转移队列接口，生产者要等消费者消费的队列，生产者尝试把元素直接转移给消费者

### LinkedTransferQueue.class，转移队列的链表实现，它比SynchronousQueue更快

## 其它容器：

### ConcurrentMap.class，并发Map的接口，定义了putIfAbsent(k,v)、remove(k,v)、replace(k,oldV,newV)、replace(k,v)这四个并发场景下特定的方法

### ConcurrentHashMap.class，并发HashMap

### ConcurrentNavigableMap.class，NavigableMap的实现类，返回最接近的一个元素

### ConcurrentSkipListMap.class，它也是NavigableMap的实现类（要求元素之间可以比较），同时它比ConcurrentHashMap更加scalable——ConcurrentHashMap并不保证它的操作时间，并且你可以自己来调整它的load factor；但是ConcurrentSkipListMap可以保证O(log n)的性能，同时不能自己来调整它的并发参数，只有你确实需要快速的遍历操作，并且可以承受额外的插入开销的时候，才去使用它

### ConcurrentSkipListSet.class，和上面类似，只不过map变成了set

### CopyOnWriteArrayList.class，copy-on-write模式的array list，每当需要插入元素，不在原list上操作，而是会新建立一个list，适合读远远大于写并且写时间并苛刻的场景

### CopyOnWriteArraySet.class，和上面类似，list变成set而已