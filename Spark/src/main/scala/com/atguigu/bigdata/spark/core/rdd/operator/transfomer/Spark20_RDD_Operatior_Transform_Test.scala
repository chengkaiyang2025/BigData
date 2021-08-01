package com.atguigu.bigdata.spark.core.rdd.operator.transfomer

import org.apache.spark.{SparkConf, SparkContext}

/**
 *求出单词最多的
 */
object Spark20_RDD_Operatior_Transform_Test {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(List(

      ("a", 1), ("a", 2), ("b", 3),
      ("b", 4), ("b", 5), ("a", 6)
    ))

    /**
     * reduceByKey:
     *  combineByKeyWithClassTag[V](
     *  (v: V) => v,// 第一个值不参与计算
     *  func,//分区内计算规则
     *  func)//分区之间的计算规则
     */
    println("reduceByKey")
    rdd.reduceByKey(_ + _).collect().foreach(println)

    /**
     *     combineByKeyWithClassTag[U](
     *     (v: V) => cleanedSeqOp(createZero(), v),初始值和第一个key的value值进行分区的操作
     *     cleanedSeqOp,分区内的计算规则
     *     combOp)分区之间的计算规则
     */
    println("aggregateByKey")
    rdd.aggregateByKey(0)((x,y) => x+y,(x,y) => x+y).collect().foreach(println)

    /**
     *     combineByKeyWithClassTag[V](
     *     (v: V) => cleanedFunc(createZero(), v),
     *     cleanedFunc,
     *     cleanedFunc)
     */
    println("foldByKey")
    rdd.foldByKey(0)(_ + _).collect().foreach(println)

    /**
     *    combineByKeyWithClassTag(
     *    createCombiner,
     *    mergeValue,
     *    mergeCombiners)
     */
    println("combineByKey")
    rdd.combineByKey((v => v),(t:Int,y) => t+y,(x:Int,y:Int) => x + y)
      .collect().foreach(println)
    sc.stop()

  }

}


