package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

/**
 *求出单词最多的
 */
object Spark24_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)

    val rdd = sc.textFile("data/agent.log")
    val maprdd = rdd.map(
      l => {
        val data = l.split(" ")
        ((data(1), data(4)), 1)
      }
    )
    val reduceRDD = maprdd.reduceByKey(_ + _)
    reduceRDD.map{
      case((prd,ad),cnt) => (prd,(ad,cnt))
    }.groupByKey()
      .mapValues( i => {
          i.toList.sortBy(_._2).reverse.take(3)
      }).collect().foreach(println)
    sc.stop()

  }

}


