package com.atguigu.chapter06

/**
 *成员属性
 */
object Test04_ClassForAccess {

}

// 定义父类
class Person {
  private var idCard: String = "222212"
  protected var name: String = "alice"
  var sex: String = "female"
  var age: Int = 12
  def printInfo(): Unit ={
    println(s"Person idCard:${idCard}, name:${name}, sex:${sex}, age:${age}")
  }
}