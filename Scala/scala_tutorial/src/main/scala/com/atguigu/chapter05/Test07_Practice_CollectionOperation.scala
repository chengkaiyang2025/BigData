package com.atguigu.chapter05

object Test07_Practice_CollectionOperation {
  def main(args: Array[String]): Unit = {
    val arr: Array[Int] = Array(12, 45, 75, 98)
    // 对数组进行操作，将操作抽象出来，然后返回一个新数组
    def arrayOperations(arr: Array[Int], func: (Int) => Int):Array[Int] = {
      for( elem <- arr) yield func(elem)
    }
    // 定义一个加一的操作
    val addOne = (a: Int) => (a + 1)
    // 调用函数
    for(e <- arrayOperations(arr, addOne)) println(e)
    // 调入匿名数据*2
    println("----------")
    for(e <- arrayOperations(arr, _ * 2 )) println(e)
  }
}
