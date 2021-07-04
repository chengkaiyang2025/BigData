package com.atguigu.chapter06

object Test06_ConstructorParams {
  def main(args: Array[String]): Unit = {
    val student2 = new Student2()
    student2.name = "alice"
    student2.age = 18
    println(s"student2: name: ${student2.name},age: ${student2.age}")
    val student3 = new Student3("alice", 22)
    student3.age = 333
    println(s"student3: name ${student3.name},age: ${student3.age}")
    val student4 = new Student4("alice", 24)
    student4.printInfo()
    val student5 = new Student5("alice", 25)
    println(s"student5: name ${student5.name}, age: ${student5.age}")
//    student5.age = 1 //error
    val student6 = new Student6("cary", 25, "atguigu")
    student6.printInfo()
  }
}

//定义类
//无参构造
class Student2{
  var name: String = _
  var age: Int = _
}

class Student3(var name: String, var age: Int)

class Student4(name: String, age: Int){
  def printInfo(): Unit ={
    println(s"student4 name = ${name},age = ${age}")
  }
}

class Student5(val name: String, val age: Int)

class Student6(var name: String, var age: Int){
  var school: String = _

  def this(name: String, age: Int, school: String){
    this(name, age)
    this.school = school
  }

  def printInfo(): Unit ={
    println(s"student6: name = ${name}, age = ${age}, school = ${school}")
  }
}
