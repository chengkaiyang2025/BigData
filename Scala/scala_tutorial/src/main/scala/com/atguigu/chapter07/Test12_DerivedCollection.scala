package com.atguigu.chapter07

object Test12_DerivedCollection {
  def main(args: Array[String]): Unit = {
    val list1 = List(1, 3, 5, 7, 9, 11, 13, 15)
    val list2 = List(3,7,2,45,4,8,9,18)

    // 获取头
    println(list1.head)
    // 获取尾巴
    println(list2.tail)
    // 最后的元素
    println(list2.last)
    // 反转
    println(list1.reverse)
    // 左三
    println(list1.take(3))
    println(list1.takeWhile( _ % 3 == 0))
    //
    println(list1.takeRight(3))
    //
    println("---------")
    println(list1.drop(3))
    println(list1.dropRight(2))
    // 并查集

    //
    println("并集合")
    val unionall = list1.union(list2)
    println(s"union all ${unionall}")

    val set1 = Set(1,2,3,4,11,1)
    val set2 = Set(2,2,1,1,3,111)
    println(s"set1 union set2 is ${set1.union(set2)}")
    println("交集")

    println(s"list1 intersect list2 is ${list1.intersect(list2)}")
    println(s"set1 intersect set2 is ${set1.intersect(set2)}")
    println("差集")
    println(s"set1 diff set2 is ${set1.diff(set2)}")
    println(s"list1 diff list2 is ${list1.diff(list2)}")
    println("拉链")
    println(s"list1 zip list2:${list1.zip(list2)}")
    println(s"list2 zip list1:${list2.zip(list1)}")
    println("滑动窗口")
    var list3 = List(1,2,3,4,5,6,7,8,9)
    list3.sliding(3).foreach(println)
    println("滑动窗口：")
    list3.sliding(4,2).foreach((i) => {
      println(i)
    })

  }
}
