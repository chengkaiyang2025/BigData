package com.atguigu.chapter07

object Test14_HighLevelFunction_Map {
  def main(args: Array[String]): Unit = {
    var list1 = List(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
    //过滤
    println(list1.filter((i) => {
      i % 2 == 0
    }))

    println(list1.filter(_ % 2==1))
    //映射
    println("-----------")
    println(list1.map(_*2))
    println(list1.map( (i) => {
      if(i % 2 == 0) "偶数" else "奇数"
    }))
    println(list1.map(x => x* x))
    //扁平化
    val list2 = List(List(1,2,3,4),List(4,2,1,0),List(1,1,1,-1,1,1))
    println(list2.flatten)
    //扁平映射
    println("=====")
    println(list2.flatMap((i) => {
      i
    }))

    val strings = List("hello world","hello tom","hello jerry","hello wsn")
    println(strings.flatMap(_.split(" ")))
    //分组group by
    //
    val list = strings.flatMap(_.split(" "))
    println(list)
    val stringToStrings = list.groupBy(data => data)
    println(stringToStrings.map((data) => {
      data._1 +":"+ data._2.length
    }))
//    println(stringToStrings)
    //
    val functionToMap = strings.flatMap(_.split(" "))
      .map((data:String) => {
        if (data.startsWith("w")) "male" else "female"
      }).groupBy(data => data)
      .map((data) => {
        s"${data._1} is ${data._2.length``                                                }"
      })
    println(functionToMap)
  }
}
