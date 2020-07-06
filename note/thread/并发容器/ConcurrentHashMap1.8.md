ConcurrentHashMap1.8

**一、核心剖析**

​	**1.核心成员变量**

​	**2.核心方法**

​		**(1).初始化**

​		**(2).get**

​		**(3).put**

​		**(4).remove**

​		**(5).size**

**二、对比**

​	**1.concurrentHashMap1.7与1.8的区别**

​	**2.HashMap与ConcurreHashMap的区别**

​	**3.HashMap与HashTable的区别**



## 一、核心剖析

​	**核心的数据机构：segement数组+链表+红黑树**

​	**并发实现：CAS+synchronized**

### 	1.核心成员变量

​		Node<K,V>[] **table**:默认为null，初始化发生第一次插入操作，默认size为16，用来存储Node节点数据，扩容时大小总是2的幂次方。

​		Node<K,V>[] **nextTable**：默认为null，扩容时新生成的数组，其大小为原数组的两倍。

​		volatile int **sizeCtl**：默认为0，用来控制table初始化和扩容操作。

​			-1：表示当前线程正在扩容操作。

​				

​		Node：保存key/value、hash值的数据结构。volatile保证了并发的可见性。

```
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K,V> next;#保证了
        .
        .
        .
        }
```



### 	2.核心方法

​		(1).**初始化**

​			ConcurrentHashMap的初始化是在put中触发的，并且只会初始化一次。初始化默认为16.

​		(2).**put**

​			Put操作采用CAS+Sync实现并发插入和更新。

​			a.首先判断table是否为空，如果为空调用initTable进行table初始化。

​			b.如果当前bucket为空，就采用cas操作将Node放到bucket中。

​			c.如果当前map正在扩容，则调用helpTransfer()，先协助扩容，再更新值。

​			d.如果出现hash冲突，则采用synchronized关键字，如果hash对应的节点是链表的头节点，遍历链表，找到对应node节点，修改val值，否则在**链表末端添加Node**,如果是红黑树，则遍历红黑树，更新或新增节点。

​			e.插入node节点后，如果链表节点超过8个，链表转为红黑树。

​			f.统计节点个数，检查是否需要扩容。

```java
    final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException();
        int hash = spread(key.hashCode());//获取key的hash值
        int binCount = 0;
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0)
                tab = initTable();//a.table扩容，只会触发一次
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {//当前table为空
                if (casTabAt(tab, i, null,
                             new Node<K,V>(hash, key, value, null)))
                    break;                   // no lock when adding to empty bin
            }
            else if ((fh = f.hash) == MOVED)
                tab = helpTransfer(tab, f);//多线程协助扩容
            else {	//出现hash冲突
                V oldVal = null;
                synchronized (f) {//给链表节点加锁
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            binCount = 1;
                            for (Node<K,V> e = f;; ++binCount) {
                                K ek;
                                if (e.hash == hash &&	// 节点已经存在，修改链表节点的值
                                    ((ek = e.key) == key ||
                                     (ek != null && key.equals(ek)))) {
                                    oldVal = e.val;
                                    if (!onlyIfAbsent)
                                        e.val = value;
                                    break;
                                }
                                Node<K,V> pred = e;
                                if ((e = e.next) == null) {	//节点不存在，就在链表尾部插入新节点
                                    pred.next = new Node<K,V>(hash, key,
                                                              value, null);
                                    break;
                                }
                            }
                        }
                        else if (f instanceof TreeBin) {//如果为红黑树，就遍历红黑树，然后更新或插入节点
                            Node<K,V> p;
                            binCount = 2;
                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                           value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                    }
                }
                if (binCount != 0) {
                    if (binCount >= TREEIFY_THRESHOLD)//如果链表长度>=8，则将链表转为红黑树
                        treeifyBin(tab, i);
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
        addCount(1L, binCount);//重新计算size,检查是否需要resize
        return null;
        }
```

​	(2). **get**

​	(3).**tabAt**

```java
static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
        return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
    }
```

采用Unsafe.getObjectVolatie()来获取，而不是直接用table[index]的原因跟ConcurrentHashMap的弱一致性有关。在java内存模型中，我们已经知道每个线程都有一个工作内存，里面存储着table的副本，虽然table是volatile修饰的，但不能保证线程每次都拿到table中的最新元素，Unsafe.getObjectVolatile可以直接获取指定内存的数据，保证了每次拿到数据都是最新的。



## 二、对比

### 1.JDK1.7与JDK1.8中ConcurrentHashMap的区别：

|          | jdk1.7                                                       | jdk1.8                                                       |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 数据结构 | Segment[]+链表                                               | segment[]+链表+红黑树                                        |
| 同步机制 | segement分段锁+ReentrantLock                                 | 分段锁+CAS+Sync                                              |
| 键值对   | HashEntry                                                    | Node                                                         |
| put实现  | 多个线程同时竞争获取同一个segment锁，获取成功的线程更新map；失败的线程尝试多次获取锁仍未成功，则挂起线程，等待释放锁 | 访问相应的bucket时，使用sychronizeded关键字，防止多个线程同时操作同一个bucket，如果该节点的hash不小于0，则遍历链表更新节点或插入新节点；如果该节点是TreeBin类型的节点，说明是红黑树结构，则通过putTreeVal方法往红黑树中插入节点；更新了节点数量，还要考虑扩容和链表转红黑树 |
| size实现 | 统计每个Segment对象中的元素个数，然后进行累加，但是这种方式计算出来的结果并不一样的准确的。先采用不加锁的方式，连续计算元素的个数，最多计算3次：如果前后两次计算结果相同，则说明计算出来的元素个数是准确的；如果前后两次计算结果都不同，则给每个Segment进行加锁，再计算一次元素的个数； | 通过累加baseCount和CounterCell数组中的数量，即可得到元素的总个数； |

2.ConcurentHashMap与HashTable的区别

（1）HashTable是强一致性的，他的同步机制是通过sync锁住整个表实现的，阻塞式，虽然数据总是最新的，但是效率太低。

（2）ConcurrentHashMap是弱一致性的，更新操作的是锁住部分数据。





