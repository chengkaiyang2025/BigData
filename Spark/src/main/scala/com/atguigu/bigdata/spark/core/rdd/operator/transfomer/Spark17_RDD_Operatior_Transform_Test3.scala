package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

/**
 *求出单词最多的
 */
object Spark17_RDD_Operatior_Transform_Test3 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(List(
      "this is a world","this is my test","test is a good test","enough is enough"
    ))
    rdd.flatMap(t => t.split(" "))
      .map(t => (t, 1))
      .foldByKey(0)((x, y) => x + y)
      .sortBy(t => t._2,false).collect().foreach(println)

    sc.stop()

  }

}


