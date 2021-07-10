package com.atguigu.chapter07

import scala.collection.mutable

object Test07_MutableSet {
  def main(args: Array[String]): Unit = {
    val set1: mutable.Set[Int] = mutable.Set(13, 12, 3, 2, 1, -1, 2, 2, 2)
    println(set1)
    set1.add(-100000)
    println(set1)
    set1+=111
    println(set1)
    set1 -= 111
    set1.remove(13)
    println(set1)
  }
}