package com.atguigu.chapter02

object Test08_DataTypeConversion {
  def main(args: Array[String]): Unit = {
    // 1.自动类型转换
    // (1)自动提升原则：有多种类型的数据混合运算的时候，系统
    // 会优先将所有类型转为精度大的类型，保证精度不丢。
    val a1: Byte = 123
    val b1: Long = 2353
    val result: Long = a1 + b1
    val re: Int  = (a1 + b1.toInt)
    println(s"result: $result,re:$re")
    // (2)把精度大的赋给精度小的会报错，
    val b2: Byte = 112
    val a2: Long = b1
//    val c: Byte = a
    // (3): byte short char 之间不会互相转换
    val a3: Byte = 10
    val b3: Char = 'b'
    val c3: Int = b3
    println(c3)
    // 2强类型转换
    //将数据由高精度转为低精度需要转换类型，如Long到int
    val n1: Int = -2.9.toInt
    println(s"n1:$n1")
    // 强类型 只作用单个变量，可以对括号提升优先级
    val n2: Int = 2.3.toInt + 2.4.toInt
    println(s"n2:${n2}")
    val n3: Int = (4.1 + 1.333).toInt
    println("n3${n3}")
    // 3、数值型和字符串的转换
    // 数值转为字符串
    val n: Int = 27
    val s: String = "this is " + n
    println(s)
    // 字符串转为数值
    val m: Int = "2".toInt
    val f: Float = "2.12".toFloat
    val f2: Int = "12.212".toDouble.toInt
    println(s"m: ${m},f:${f},f2:${f2}")
  }
}
