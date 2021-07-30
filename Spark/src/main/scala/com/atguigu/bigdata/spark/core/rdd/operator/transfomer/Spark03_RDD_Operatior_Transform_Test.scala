package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("test")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(Seq(1, 2, 3, 4), 2)
    val mrRdd = rdd.mapPartitionsWithIndex(
      (index,iter) => {
        if(index == 1){
          iter
        }else{
          Nil.iterator
        }
      }
    )
    mrRdd.collect().foreach(println)
    sc.stop()
    
  }
}
