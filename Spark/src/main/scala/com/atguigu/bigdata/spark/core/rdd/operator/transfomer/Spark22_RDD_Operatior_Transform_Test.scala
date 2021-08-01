package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

/**
 *求出单词最多的
 */
object Spark22_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(List(

      ("a", 1), ("a", 2), ("b", 3),
    ))
    val rdd2 = sc.makeRDD(List(
      ("a", -2), ("b", -1), ("b", -3)
    ))
    rdd.rightOuterJoin(rdd2).collect().foreach(println)

    sc.stop()

  }

}


