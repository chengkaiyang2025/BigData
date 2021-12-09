package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

/**
 *交集并集补集
 */
object Spark15_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(List(
      ("abc", 1), ("efg", 2), ("abc", 1), ("hbd", 11)
    ))
    rdd.reduceByKey((x:Int, y:Int) => {
      x +y
    }).collect().foreach(println)
    println("---")
    rdd.map(t => (t._1,t)).reduceByKey((x,y) => {
      (x._1+y._1,x._2+x._2)

    }).collect().foreach(println)
    sc.stop()
  }

}


