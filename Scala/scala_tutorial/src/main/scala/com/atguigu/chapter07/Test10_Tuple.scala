package com.atguigu.chapter07

object Test10_Tuple {
  def main(args: Array[String]): Unit = {
    val tuple = ("hello", 100, 'a', true)
    println(tuple.productElement(1))
    println(tuple._1)
    println(tuple._2)

    println("---遍历----")
    for(elem <- tuple.productIterator){
      println(elem)
    }
    println("嵌套数组")
    val tuple2 = (12,0.3,("a",true),1)
    println(tuple2._3._1)
  }
}
