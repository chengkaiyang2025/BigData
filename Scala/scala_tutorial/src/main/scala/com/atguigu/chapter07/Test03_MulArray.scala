package com.atguigu.chapter07

object Test03_MulArray {
  def main(args: Array[String]): Unit = {
    val array = Array.ofDim[Int](n1 = 2, n2 = 2)
    array(1)(1) = 1
    array(0)(0) = 3
//    println(array.mkString("-"))
    for(i <- 0  until array.length; j <- 0 until array(i).length){
      println(array(i)(j))
    }
    println("---print-----")
    for(i <- array.indices){
      for(j <- array(i).indices){
        print(array(i)(j))
        print(" ")
      }
      println()
    }

  }
}
