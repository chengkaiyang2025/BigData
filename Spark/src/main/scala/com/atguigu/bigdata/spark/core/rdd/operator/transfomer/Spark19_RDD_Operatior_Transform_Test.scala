package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 *求出单词最多的
 */
object Spark19_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(List(

      ("a", 1), ("a", 2), ("b", 3),
      ("b", 4), ("b", 5), ("a", 6)
    ))

    val newRDD:RDD[(String,(Int,Int))] = rdd.combineByKey(
      v => (v, 1),
      (t: (Int, Int), v) => {
        (t._1 + v, t._2 + 1)
      },
      (t1: (Int, Int), t2: (Int, Int)) => {
        (t1._1 + t2._1, t2._2 + t2._2)
      }
    )
    newRDD.mapValues {
      case (s, cnt) => s / cnt
    }.collect().foreach(println)

    sc.stop()

  }

}


