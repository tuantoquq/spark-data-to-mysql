package com.admicro.utils.config

import com.typesafe.config.ConfigFactory

import java.io.File

object ConfigInfo extends Serializable {
  val config = ConfigFactory.parseFile(new File("config.properties"))

  //app config
  val appName: String = config.getString("app.name")
  val offset : Int = config.getInt("app.offset")
  val beginDate: String = config.getString("app.date.begin")
  val endDate: String = config.getString("app.date.end")

  //hdfs input location
  val hdfsCpaInput: String = config.getString("hdfs.cpa.input")

  //hdfs output location
  val hdfsCpaOutput: String = config.getString("hdfs.cpa.output")

  //mysql configuration
  val mysqlUrl: String = config.getString("mysql.url")
  val mysqlUserName: String = config.getString("mysql.username")
  val mysqlPassWord: String = config.getString("mysql.password")
  val mysqlTable: String = config.getString("mysql.table.cpa")
  val mysqlDriver: String = config.getString("mysql.driver")


}
