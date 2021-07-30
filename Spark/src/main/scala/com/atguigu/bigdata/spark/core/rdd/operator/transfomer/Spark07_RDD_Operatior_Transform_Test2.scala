package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark07_RDD_Operatior_Transform_Test2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)
    val rdd = sc.textFile("data/apache.log")
    rdd.filter(t =>
      {
        val strings = t.split(" ")
        t.split(" ").takeRight(1).endsWith(".css")
      }
  ).collect().foreach(println)
    sc.stop()
  }
}
