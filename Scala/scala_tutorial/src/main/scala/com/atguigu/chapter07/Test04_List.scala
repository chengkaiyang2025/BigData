package com.atguigu.chapter07

import scala.::

object Test04_List{
  def main(args: Array[String]): Unit = {
    val list1 = List(1, 2, 3, 4, 5)
    println(list1)
    // 遍历
    list1.foreach((i) => (println(i+1)))
    println("------")
    var s: Int = 0;
    list1.foreach((i) => (s = s + i))
    println(s)

    def a(i:Int):Int = {
      if(i % 2== 0) return 0
      println(s"${i}")
      i
    }

    println("----some ----")
    var s1:Int = 0
    list1.foreach((i) => {
      if(i % 2 ==0 ) println(s"偶数 ${i}") else println(s"奇数 ${i}")
    })
    println(s1)
    // 3、添加元素
    val list2 = -100 +: list1 :+ 12 :+ 11
    println(list2)
    // 4、合并列表
    val list6 = 73 :: 32 :: Nil
    val list7 = 11 :: -100 :: -333 :: Nil
    val list8 = List(11,11,22,222)
    val list9 = list6 ::: list7 ::: list8
    println(list9.mkString("-"))

  }
}
