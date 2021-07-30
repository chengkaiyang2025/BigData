package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark04_RDD_Operatior_Transform_MatchCase {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("test")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(List(
                            List("this is a", "this is b", "this is c"),
                            List(1,2,3,4,5,1),
                            List(List(100,1000),List("ssssss","ssssssss")),
                            List("this is ab", "this is b", "this is c")
    ))
    rdd.flatMap(
      t => {
        t match {
//          case l:List[Any] => l
          case t => t
        }
      }
    ).flatMap(
      t => {
        t match {
          case l:List[Any] => l
          case s:String => s.split(" ")
          case t => List(t)
        }
      }
    ).collect().foreach(println)
    sc.stop()

  }
}
