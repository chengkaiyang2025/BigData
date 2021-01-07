# 第一章、HBase简介

## 1.1 、HBase定义

​	HBase是一种分布式、可扩张、支持海量存储的NoSql数据库**。

## 1.2、HBase数据模型

​	逻辑上HBase和rdbms数据库很像，有行有列。

​	而在物理存储上，更像k-v的multi-dimensional map。

### 1.2.1、HBase逻辑模型

![](/home/yzf/IdeaProjects/Bigdata/HBase/学习过程/img/Hbase逻辑架构.png)



### 1.2.2、HBase物理存储结构

![](/home/yzf/IdeaProjects/Bigdata/HBase/学习过程/img/HBase数据模型.png)

### 1.2.3、数据模型

#### 1、NameSpace

​	命名空间，类似于关系型数据库的DataBase概念，每个命名空间下有多个表。HBase有两个自带的命名空间。分别是hbase、default，hbase中存放的是hbase内置的表，default表是用户默认使用的命名空间。

#### 2、Region

​	类似于关系型数据库的概念，定义表的时候，定义**列族**即可。

#### 3、Row

​	HBase表中的每行都由一个Rowkey和多个Column组成。Rowkey有字典序存储。查询时候只能按照Rowkey进行检索，因此Rowkey的设计十分重要。

#### 4、Column

​	HBase中每个Column分为Column Family（列族）与Column Qualifier（列限定符）进行限定，如info:name,info:gender。

#### 5、TimeStamp

​	时间戳，用于标注数据的不同版本（version）， 每条数据写入时候，如果不指定时间戳，系统会自动为其加上时间戳字段，写入到HBase中。

#### 6、Cell

​	cell由{rowkey:column family:column quailify:timestamp}来指定唯一的Cell格。HBase主要存储稀疏矩阵，因此他的逻辑模型和物理存储模型是不一样的。



## 1.3、HBase基本架构（不完整版本）

![](/home/yzf/IdeaProjects/Bigdata/HBase/学习过程/img/简单的架构.png)

架构角色

### 1.3.1、Region Server

Region Server作为Region主要的管理者，实现类为HRegionServer，主要作用如下：

- 对于数据：put、get、delete
- 对于Region：主要合并、切分：splitRegion、compactRegion

### 1.3.2、Master

Master是所有RegionServer的

### 1.3.3、Zookeeper

### 1.3.4、HDFS







# 第二章、HBase快速入门

## 2.1、HBase安装部署

略

## 2.2、HBase Shell操作

### 2.2.1、基本操作

### 2.2.2、表的操作

# 第三章、HBase进阶

## 3.1、架构原理

## 3.2、写原理

## 3.3、MemStore Flush

