package com.atguigu.chapter06

/**
 *匿名类，
 */
object Test10_AnnoymousClass {
  def main(args: Array[String]): Unit = {
    val person = new Person10 {
      override var name: String = "abc"

      override def eat(): Unit = {
        println("He like eating.")
      }
    }
    println(person.name)
    person.eat()
  }
}
abstract class Person10{
  var name: String
  def eat(): Unit
}