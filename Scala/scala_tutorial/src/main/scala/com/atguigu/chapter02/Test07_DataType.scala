package com.atguigu.chapter02

import com.atguigu.chapter01.Student
;

object Test07_DataType {
  def main(args: Array[String]): Unit = {
  // 1、整数类型
    val a: Byte = 127
    val b: Byte = -128

    val a3 = 127
    val a4: Long = 123133L

    val b1: Byte = 10
    val b2: Byte = (10 + 10).toByte
    println(b2)

    val b3: Byte = (b1 + 20).toByte
    println(b3)
    // 2. 浮点类型
    val f1: Float = 1.234f
    val d1 = 1.234
    // 3.字符类型
    val c1: Char = 'a'
    println(c1)

    val c2: Char = '0'
    println(c2)
    // 控制字符
    val c3: Char = '\t'
    val c4: Char = '\n'
    println(s"this is char ${c1}.${c4}this is number ${c3}${c2}.${c4}")
    // 转义字符
    val c5 = '\\'
    val c6 = '\"'
    println(s"转义字符：${c5}，双引号：${c6}")
    //字符变量底层的ASCII码
    val i1: Int = c1
    println(s"a: ${i1}")
    val i2: Int = c2
    println(s"0: ${i2}")

    val c7: Char = (i1 + 1).toChar
    val c8: Char = (i2 + 1).toChar
    println(c7)
    println(c8)
    // 4、布尔类型
    val isTrue: Boolean = true
    println(s"isTure: ${isTrue}")
    // 5、空类型
    def m1(): Unit = {
      println("你好")
    }

    val a1 = m1()
    println(s"函数调用结果：${a1}")
    // 5.2、空引用NUll
    var alice = new Student("alice", 23)
    alice = null
    println(alice)

    // 5.3、Nothing
    def m2(i: Int): Int = {
      if (i == 0){
        throw new NullPointerException
      }else{
        return i
      }
    }
    val b22:Int = m2(2)
    println(b22)
  }
}
