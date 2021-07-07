package com.atguigu.chapter07

/**
 * 不可变列表
 */
object Test01_ImmutableArray {
  def main(args: Array[String]): Unit = {
    // 1、创建数据
    val arr: Array[Int] = new Array[Int](5)
    // 方式2
    val arr2: Array[Int] = Array(12, 37, 42, 58, 97)
    // 2、访问数组
    println(arr)
    println(arr(0))
    println(arr(1))
    println(arr2(0))
    println(arr2(arr2.length - 1))
    println(arr2(2))
    // 3、数组的遍历
    // 最直接方式
    for(i <- arr2){
      println(s"this is ${i}")
    }
    // 下标遍历
    for(i <- 0 until arr2.length){
      println(s"${i} is ${arr2(i)}")
    }
    // 遍历下标
    for(i <- arr2.indices) println(s"${i} is the index of ${arr2(i)}")
    // 迭代器模式
    val iterator = arr2.iterator
    println("_____")
    while (iterator.hasNext){
      println(iterator.next())
    }
    println("-----------")
    iterator.foreach((i) =>(println(s"${i} foreach,${i}")))
    println("-----")
    arr2.foreach((elem: Int) => (println(s"${elem}")))

    println(arr2.mkString(",->"))
    // 4、添加元素 :冲着 变量
    println("---------添加元素")
    val arr3 = arr2 :+ 100
    println(arr3.mkString(","))
    val arr4 = -100 +: -99 +: -80 +: arr3 :+ 1000 :+ 10000
    for(i <- arr4.indices){
      println(s"${i} is ${arr4(i)}")
    }
    println("--------------------------------------------")
    println(arr4.reverse.mkString(","))


  }
}
