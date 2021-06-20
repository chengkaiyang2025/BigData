package com.atguigu.chapter04

import scala.util.control.Breaks
import scala.util.control.Breaks.break

object Test06_Break {
  def main(args: Array[String]): Unit = {
    Breaks.breakable(
      for(i <- 0 until 5){
        println(i)
        if(i == 3) break()
      }
    )
  }
}
