HashMap中的hash冲突及死循环

1. **什么是hash?**

   主要思想是根据结点的关键码值来确定其存储地址，以关键值k作为自变量，通过散列函数，计算出对应值，把这个值解释为结点的存储地址，将节点存储到此存储单元中。检索时，用同样的方法计算出地址，然后到对应的单元取出结点。通过散列可以对结点进行快速检索

   长点散列函数的算法：除余法、乘余取整法、平方取中法

2. **hash冲突解决方法**

   ​	两个方向：开散列法（open hashing）、闭散列法（close hashing，或者开地址法）

   ​	两者区别：开散列发就是把发生冲突的key存储在散列表主表外，而闭散列法则是把发生冲突的key存储在散列表的其他地址内。

   1. 分离链表法（拉链法）

      简单形式就是把散列表都中的每个槽定义为一个链表的表头，然后把散列到相同的结点存放在这个槽的链表中。每个槽中存储的是一个value和一个指向链表其余部分的指针。

      如下图：77和110的散列值相同，7、95、62的散列值相同

      ![并发concurrentHashMap - 拉链法](C:\Users\zhanyu.gu\Desktop\享学\笔记\picture\并发concurrentHashMap - 拉链法.jpg)

   2. 闭散列法

      简单思路即，如果两个节点的散列值冲突的时候，会重新计算（根据指定的策略）并寻找新的槽地址

      例如：线性探测法、二次探查法、随机探测法、双散列探测法

   

3. **HashMap的死循环（死锁）**

   稍等

   1. 

      **出现场景**：当hashMap中的key出现hash冲突，并且在多线程的环境中，两个线程同时触发扩容操作的时候，链表容易出现循环引用，如果触发get操作的时候从而造成出现死锁。

      eg: 现在有一个size为2的HashMap,有三个节点，对应的key分别得3,5,7，所以需要扩容，现有两个线程，在put操作的时候，t1和t2同时触发resize()扩容，然后调用transfer()，迁移原hash表中的数据到新的hash表中，会出现链表中指针互相指引死循环，然后get()当前链表的其他节点值的时候就会陷入死循环，从而造成死锁。

      **源码如下：**

      (1).put操作

   ```java
   public V put(K key, V value)
   {
       ......
       //算Hash值
       int hash = hash(key.hashCode());
       int i = indexFor(hash, table.length);
       //如果该key已被插入，则替换掉旧的value （链接操作）
       for (Entry<K,V> e = table[i]; e != null; e = e.next) {
           Object k;
           if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
               V oldValue = e.value;
               e.value = value;
               e.recordAccess(this);
               return oldValue;
           }
       }
       modCount++;
       //该key不存在，需要增加一个结点
       addEntry(hash, key, value, i);
       return null;
   }
   ```

   ​	(2).检测容量是否超标

   ```
   void addEntry(int hash, K key, V value, int bucketIndex)
   {
       Entry<K,V> e = table[bucketIndex];
       table[bucketIndex] = new Entry<K,V>(hash, key, value, e);
       //查看当前的size是否超过了我们设定的阈值threshold，如果超过，需要resize
       if (size++ >= threshold)
           resize(2 * table.length);
   }
   ```

   (3).对数组扩容，新建一个更大的hash表，然后把数据从原来的hash表中迁移过来

   ```
   void resize(int newCapacity)
   {
       Entry[] oldTable = table;
       int oldCapacity = oldTable.length;
       ......
       //创建一个新的Hash Table
       Entry[] newTable = new Entry[newCapacity];
       //将Old Hash Table上的数据迁移到New Hash Table上
       transfer(newTable);
       table = newTable;
       threshold = (int)(newCapacity * loadFactor);
   }
   ```

   (4).迁移数据

   ```java
   void transfer(Entry[] newTable)
   {
       Entry[] src = table;
       int newCapacity = newTable.length;
       //下面这段代码的意思是：
       //  从OldTable里摘一个元素出来，然后放到NewTable中
       for (int j = 0; j < src.length; j++) {
           Entry<K,V> e = src[j];
           if (e != null) {
               src[j] = null;
               do {
                   Entry<K,V> next = e.next;
                   int i = indexFor(e.hash, newCapacity);
                   e.next = newTable[i];
                   newTable[i] = e;
                   e = next;
               } while (e != null);
           }
       }
   }
   ```

   **注意：在这行代码处，Entry<K,V> next = e.next;，如果是多线程环境下，此处容易出现死循环。**

   [详细请参考]: https://www.cnblogs.com/jing99/p/11319175.html	"hashmap死循环详解"

   

4. **JDK8优化**

   JDK 8 中采用的是位桶 + 链表/红黑树的方式，当某个位桶的链表的长度超过 8 的时候，这个链表就将转换成红黑树

   HashMap 不会因为多线程 put 导致死循环（JDK 8 用 head 和 tail 来保证链表的顺序和之前一样；JDK 7 rehash 会倒置链表元素），但是还会有数据丢失等弊端（并发本身的问题）。因此多线程情况下还是建议使用 ConcurrentHashMap。

   

5. **为什么线程不安全**

- 多线程环境下如果出现hash冲突，hash值相同的节点，存储在同一位置，导致数据相互覆盖。
- 多线程环境下的扩容，会导致其他线程的扩容数据丢失。



​		





