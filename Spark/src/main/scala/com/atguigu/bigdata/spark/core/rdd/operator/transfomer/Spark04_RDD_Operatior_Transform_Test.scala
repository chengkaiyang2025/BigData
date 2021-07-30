package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark04_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("test")
    val sc = new SparkContext(conf)
    val rdd:RDD[List[Int]] = sc.makeRDD(List(List(1, 2, 3, 4), List(1, 2, 3, 4), List(3, 4, 5)))
    val rdd2 = rdd.flatMap(t => t)
    val result = rdd2.map(t => {
      if (t % 2 == 0) (t, "偶数") else (t, "奇数")
    })
    result.collect().foreach(println)
    sc.stop()

  }
}
