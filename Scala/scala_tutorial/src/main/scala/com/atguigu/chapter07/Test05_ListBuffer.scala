package com.atguigu.chapter07

import scala.collection.mutable.ListBuffer

object Test05_ListBuffer {
  def main(args: Array[String]): Unit = {
    val list1:ListBuffer[Int] = new ListBuffer[Int]()
    val list2 = ListBuffer(1,2,3,4,5,6,7)
    list1.append(11)
    list1.prepend(111)
    list1.foreach((i:Int) => {
      println(i)
      i + 1
    })
    list1.insert(1,2,3,4,5)
    list1.insertAll(0, list2)
    println(list1)
    println(list2)

    //
    println("====================")
    31 +=: 98 +=: list1
    println(list1)
    val list = list1.toList
    println(list)
    list1.remove(2)
    println("---------")
    println(list1)
  }
}
