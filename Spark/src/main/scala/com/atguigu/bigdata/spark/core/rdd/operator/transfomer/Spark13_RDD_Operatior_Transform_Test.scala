package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

/**
 *交集并集补集
 */
object Spark13_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)
    val rdd1 = sc.makeRDD(List(1, 3, -1, 3, 4, 1, 111, 11, 1, 1, 2))
    val rdd2 = sc.makeRDD(List(100, 101, -1, 2, 3, 2, 3, -1))
    println("以下为交集")
    val ints = rdd1.intersection(rdd2).collect()
    println(ints.mkString("->"))
    println("以下为并集")
    println(rdd1.union(rdd2).collect().mkString("->"))
    println("以下为去重并集")
    println(rdd1.union(rdd2).distinct().collect().mkString("->"))
    println("以下为差集")
    println(rdd1.subtract(rdd2).distinct().collect().mkString("->"))
    val rdd31 = sc.makeRDD(List(1, 2, 3))
    val rdd3 = sc.makeRDD(List("ab", "c", "d"))
    println("以下为zip")
    println(rdd31.zip(rdd3).collect().mkString("->"))
    sc.stop()
  }

}
