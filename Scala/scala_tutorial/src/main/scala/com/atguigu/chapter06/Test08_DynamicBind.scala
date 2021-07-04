package com.atguigu.chapter06

/**
 *动态绑定，体现了面向对象中的多态属性。
 */
object Test08_DynamicBind {
  def main(args: Array[String]): Unit = {
    val student: Person8 = new Student8
    println(student.name)
    println(student.age)
    student.printInfo()
  }
}
class Person8{
  var name: String = "person"
  var age: Int = 19

  def printInfo():Unit = {
    println(s"name ${name},age ${age}")
  }
}
class Student8 extends Person8{
  this.name = "student"
  this.age = 11

  override def printInfo(): Unit = {
    println(s"student ${name},${age}")
  }
}