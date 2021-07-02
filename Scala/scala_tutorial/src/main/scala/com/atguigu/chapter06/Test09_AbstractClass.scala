package com.atguigu.chapter06

/**
 *抽象类
 */
object Test09_AbstractClass {
  def main(args: Array[String]): Unit = {
    val student = new Student9()
    student.sleep()
  }
}
abstract class Person9{
  var name: String = "people"
  var age: Int

  // 非抽象方法
  def eat(): Unit = {
    println("people is eating")
  }
  // 抽象方法
  def sleep(): Unit
}
class Student9 extends Person9{
  override var age: Int = 20

  override def sleep(): Unit = {println("sleep at classroom")}
}
