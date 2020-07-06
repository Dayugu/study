Mysql调优

### 1.like如何使用索引

​	众所周知，在mysql中如果是 like '%str%'的写法是无法利用mysql的索引的，如果想用索引的话就必须满足 like 'str%' ,但是这样就不能满足模糊匹配的需求，现在有个写法既能满足需求，又能使用索

```mysql
#表
CREATE TABLE `t_user` (
  `id` varchar(40) COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `login` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '登录名称',
  `name` varchar(40) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `t_user_login_idx` (`login`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户表';

#根据login进行模糊查询
SELECT * from t_user u INNER JOIN(SELECT id from t_user where login like '%test%') s on s.id=u.id;
```

| id   | select_type | table  | type   | possibile_keys    | key              | key_len | ref                 | rows  | filtered | Extra                    |
| ---- | ----------- | ------ | ------ | ----------------- | ---------------- | ------- | ------------------- | ----- | -------- | ------------------------ |
| 1    | SIMPLE      | t_user | index  | PRIMARY,id_UNIQUE | t_user_login_idx | 1023    |                     | 48358 | 11.11    | Using where; Using index |
| 1    | SIMPLE      | u      | eq_ref | PRIMARY,id_UNIQUE | PRIMARY          | 162     | user.tlmall_user.id | 1     | 100      |                          |

explain如上所示

在这个sql中，利用mysql的覆盖索引，用like模糊查询，查出匹配的user主键ID,避免了回表的操作，然后inner join 根据主键ID查询匹配的user行数据。**在mysql中like能够使用覆盖索引进行查询。**

​		