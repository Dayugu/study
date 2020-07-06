并发容器ConcurrentHashMap

## 一、JDK1.7的ConcurrentHashMap

#### 1.初始化

​	结构图：<img src="C:\Users\zhanyu.gu\Desktop\享学\笔记\picture\ConcurrentHashMap 结构.jpg" alt="ConcurrentHashMap 结构" style="zoom:67%;" />

利用Segement实现分段锁，初始化的时候只会初始化segment[n].table[0]。HashEntry就是键值对。

#### 2.get

#### 3.put

#### 4.rehash

#### 5.remove

#### 6.size