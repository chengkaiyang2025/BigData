package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark12_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)
    val rdd = sc.makeRDD(List(1, 2,11,-11,"a",2,"abc",3,11,1,33,4,1),2)
    rdd.sortBy(t => {
      t match {
        case i:Int => i
        case s:String => s.length
      }
    }, false).saveAsTextFile("output/Spark12_RDD_Operatior_Transform_Test")
    sc.stop()
  }

}
