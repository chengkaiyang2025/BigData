package com.atguigu.chapter06

import com.atguigu.chapter01.Student

/**
 *构造器，辅助构造器
 */
object Test05_Constructor {
  def main(args: Array[String]): Unit = {
    val student1 = new Student1
    println(" ----------- ")
    student1.Student1()
    println(" ----------- ")
    val student2 = new Student1("alice")
    println(" ----------- ")
    val student3 = new Student1(name = "bob", age = 25)
    println(" -----------")
  }
}
class Student1(){
  var name: String = _
  var age: Int = _

  println("1、主构造器运行 Student1")

  def this(name: String){
    this()
    println("2、辅助构造器 this(name: String) 运行")
    this.name = name
    println(s"name: ${name}, age: ${age}")
  }

  def this(name: String, age: Int){
    this(name)
    println("3、辅助构造器 this(name: String, age: Int) 运行")
    this.age = age
    println(s"name: ${name}, age: ${age}")
  }
  def Student1(): Unit ={
    println()
  }
}
