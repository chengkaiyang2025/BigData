package com.atguigu.chapter07

object Test13_SimpleFunction {
  def main(args: Array[String]): Unit = {
    var list1 = List(6,3,5,3,8,2,5,3)
    var list2 = List(("a",1),("c",3),("d",-1),("j",11),("z",-12))
    // 求和
    println(list1.sum)
//    println(s"list2 sum is ${list2.sum(i -> i._2)}")
    //求乘积
    println(list1.product)
    //求最大值
    println(list1.max)
    println(list2.maxBy((t:(String,Int)) => (t._2)))
    //最小值
    println(list1.min)
    println(list2.minBy((t:(String,Int)) => (t._2)))
    println(list2.minBy(_._2))
    //排序
    println("----排序")
    println(list1.sorted)
    println(list1.sorted.reverse)
    //逆序排序

    //sort by
    println(s"list2 sort by _2 ${list2.sortBy((i) => (i._2))}")
    println(s"list2 sort by _1 ${list2.sortBy(_._1)}")
    //sort with
    println(list2.sortWith((a,b) => {
      a._2 > b._2
    }))
  }
}
