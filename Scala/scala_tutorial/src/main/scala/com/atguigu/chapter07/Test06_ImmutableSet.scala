package com.atguigu.chapter07

object Test06_ImmutableSet {
  def main(args: Array[String]): Unit = {
    val set1 = Set(1, 1, 2, 2, 3, -9, 1111, 2, 232, 2)
    println(set1)
    set1.foreach((i:Int) => {
//      println(s"${i+1}"
      if(i %2 == 0) println(i)
    })
    println(set1.mkString("-"))
    // 2添加元素
    val set2 = set1 + 129
    println(set2)
    val set3 = set2 - 1111
    println(set3)
    val set4 = Set(-10000,-9999,-9998,-977777)
    val set5 = set1 ++ set4
    println(set5)

  }
}
