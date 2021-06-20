package com.atguigu.chapter04

object Test03_PrintSome {
  def main(args: Array[String]): Unit = {
    for(i <- 1 to 10){
      println()
      for(j <- 1 to i){
        print(s"${i}*${j} = " + i * j+" ")
      }
    }
  }
}
