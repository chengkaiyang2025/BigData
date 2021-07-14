package com.atguigu.chapter07

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Test17_CommonWordCount {
  def main(args: Array[String]): Unit = {

    val tupleList: List[(String, Int)] = List(
      ("hello", 1),
      ("hello world", 2),
      ("hello scala", 3),
      ("hello spark from scala", 1),
      ("hello flink from scala", 2)
    )

    val newStringList: List[String] = tupleList.map(
      kv => {
        (kv._1.trim + " ") * kv._2
      }
    )
    println(newStringList)
    println(newStringList.flatMap(_.split(" "))
      .groupBy(a => a)
      .map(a => (a._1, a._2.length))
      .toList.sortBy(_._2).reverse)
    println("===方法2")
    val a =
      tupleList.flatMap(kv => {
        val strings = kv._1.split(" ")

        strings.map(a => (a, kv._2))
      })
        .groupBy(_._1)
        .mapValues(t => t.map(_._2).sum).toList.sortBy(_._2)
    println(a)
  }
}
