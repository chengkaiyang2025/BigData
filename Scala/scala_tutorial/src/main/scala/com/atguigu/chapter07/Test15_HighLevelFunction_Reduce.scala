package com.atguigu.chapter07

object Test15_HighLevelFunction_Reduce {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3,4,5)
    println(list.reduce((a, b) => {
      a + b
    }))

    println(list.reduceLeft(_ + _))
    println(list.reduceRight(_ + _))

    println(list.reduceLeft(_ - _))
    println(list.reduceRight(_ - _))

    val strings = List("this is amy", "this is yck", "this is yck 's wife", "this is wife 's wifi")
    println("-----")
    val i = strings.flatMap(_.split(" "))
      .map((a) => {
        (a, 1)
      }).reduce((a, b) => {
      ( "",
        a._2 + b._2)
    })
    println(i)

    val strings2 = List(1,2,3,4,5,6,7,9)
    println(strings2.fold(10)(_ + _))
  }
}
