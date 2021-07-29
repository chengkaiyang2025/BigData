package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark06_RDD_Operatior_Transform_Test2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(List("word","world","count","cut"))
    val re = rdd.flatMap(t => {
      t.iterator
    }).groupBy(t => t).map(t => (t._1, t._2.size))
    re.collect().foreach(println)
    sc.stop()
  }
}
