package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_Operation_Transform_MatchMap {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Spark02_RDD_Operation_Transform")
    val sc = new SparkContext(conf)
    val inputs = sc.makeRDD(List(1,"ss","this this is",List(1,2,4,5,5),List("s","s")))
    inputs.map(t => {
      t match {
        case t:Int => (s"${t}位数为：",t/10)
        case s:String => (s"${s}中单词个数为：",s.split(" ").length)
        case list:List[Int] => (s"${list}中数字个数为：",list.length)
        case list:List[String] => (s"${list}中单词个数为：",list.length)
      }
    }).collect().foreach(println)
    sc.stop()
  }
}
