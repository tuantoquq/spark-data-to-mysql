package com.admicro

import java.time.LocalDate
import java.util.Calendar

object Test {
  def main(args: Array[String]): Unit = {
    val time = Calendar.getInstance().getTime
    val hour = time.getHours
    println(time)
    println(hour)
  }
}
