package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark05_RDD_Operatior_Transform_glom {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("test")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(List("this is a", "this is b", "this is c","this is abc"),
      2)
    rdd.glom().collect().foreach(data => println(data.mkString("->")))
    println("====")
    rdd.collect().foreach(println)
    sc.stop()
  }
}
