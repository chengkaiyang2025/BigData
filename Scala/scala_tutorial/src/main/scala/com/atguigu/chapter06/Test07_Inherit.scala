package com.atguigu.chapter06

/**
 *继承
 */
object Test07_Inherit {
  def main(args: Array[String]): Unit = {
    val student1: Student7 = new Student7("alice", 29)
    student1.printInfo()
    println("----------")
    val student2: Student7 = new Student7("bob", 20, "std001")
    student2.printInfo()
    println("----------")
    val teacher = new Teacher
    teacher.printInfo()
  }
}
class Person7(){
  var name: String = _
  var age: Int = _
  println("父类方法调用")
  def this(name: String, age: Int){
    this()
    println("2、父类的辅助构造器调用")
    this.name = name
    this.age = age
  }

  def printInfo(): Unit = {
    println(s"Person ${name} ${age}")
  }
}

class Student7(name: String, age: Int) extends Person7(name: String, age:Int){
  var stdNo: String = _
  println("3.子类的主构造器方法")
  def this(name: String, age: Int, stdNo: String){
    this(name,age)
    println("4.子类的辅助构造方法")
    this.stdNo = stdNo
  }

  override def printInfo(): Unit = {
    println(s"Student ${name} ${age} ${stdNo}")
  }

}

class Teacher extends Person7{
  override def printInfo(): Unit = {
    println("teacher")
  }
}