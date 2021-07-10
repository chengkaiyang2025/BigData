package com.atguigu.chapter07

import scala.collection.mutable
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
    println("------------")
    for(i <- arr2) println(s"${i} ")

    // 2、访问元素与修改元素
    println("-修改----")
    println(arr2(1))
    arr2(1) = 5900
    println(arr2)
    // 3、添加元素
    println("-----添加元素-----")
    arr2.append(18990)
    arr2.prepend(-99)
    println(arr2)
    arr2.insert(2,25)
    println(arr2)
    println("--------添加一整个数组--------")
    val newArr:ArrayBuffer[Int] = ArrayBuffer(-10000,-1000,-9999)
    arr2.insertAll(2,newArr)
    arr2.insertAll(0,newArr)
    println(arr2)

    // 4、删除数组
    println("------删除-----")
    arr2.remove(3)
    println(arr2)
    println("删除3")
    arr2.remove(3)
    println(arr2)
    arr2.remove(3,2)
    println(arr2)
    //
    println("------- 转数组--------")
    val a: Array[Int] = arr2.toArray
    for(i <- a) println(i)
    println(a.mkString("---"))
    println("------- 转可变 --------")
    val buffer:mutable.Buffer[Int] = a.toBuffer
    println(buffer)
    println(a.mkString("->"))
  }
}
