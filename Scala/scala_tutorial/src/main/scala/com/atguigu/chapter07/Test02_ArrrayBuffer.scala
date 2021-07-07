package com.atguigu.chapter07

import scala.collection.mutable.ArrayBuffer

object Test02_ArrrayBuffer {
  def main(args: Array[String]): Unit = {
    val arr1: ArrayBuffer[Int] = new ArrayBuffer[Int]()
    val arr2: ArrayBuffer[Int] = ArrayBuffer(23, 57 ,92)
    println(arr1)
    println(arr2)
    println("----- 遍历")

    arr2.foreach((i) => (println(i)))
    for(i <- arr2.indices){
      println(s"${i} is ${arr2(i)}")
    }
  }
}
