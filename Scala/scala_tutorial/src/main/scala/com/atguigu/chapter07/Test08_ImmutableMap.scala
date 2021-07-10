package com.atguigu.chapter07

object Test08_ImmutableMap {
  def main(args: Array[String]): Unit = {
    //创建map
    val map:Map[String, Int] = Map("a" -> 13, "b" -> 14, "c" -> 188)
    println(map)
    for(k <- map.keySet){
      println(map(k))
//      map.get(k)
    }
    map.foreach((kv: (String, Int)) => {
      println(s"key is ${kv._1},value is ${kv._2}")
    })

    for(k <- map.keys){
      println(map.get(k))
    }

    println(map.get("b"))
  }
}
