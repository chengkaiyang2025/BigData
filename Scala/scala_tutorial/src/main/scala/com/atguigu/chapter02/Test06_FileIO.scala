package com.atguigu.chapter02

import java.io.{File, PrintWriter}
import scala.io.Source

object Test06_FileIO {
  def main(args: Array[String]): Unit = {
    // 输入读取
    Source.fromFile("src/main/resources/test.txt").foreach(print)
    // 数据写入
    val writer = new PrintWriter(new File("src/main/resources/output.txt"))
    writer.write("hello from write")
    writer.close()
  }
}
