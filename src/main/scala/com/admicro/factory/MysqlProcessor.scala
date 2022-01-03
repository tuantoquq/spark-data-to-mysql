package com.admicro.factory

import com.admicro.utils.config.{ConfigDate, ConfigInfo, CustomLog}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import java.sql.{Connection, DriverManager}
import java.util.Properties

object MysqlProcessor {
  val configDate = new ConfigDate
  def save(df: DataFrame, table: String, url: String, prop: Properties): Unit = {
    df.write
      .mode(SaveMode.Append)
      .jdbc(url,table,prop)
    CustomLog.printLog("save data to table " + table + " success")
  }
  def deleteData(table: String, url: String, user: String, pass: String, driver: String): Unit={
    var conn: Connection = null
    try{
      Class.forName(driver)
      conn = DriverManager.getConnection(url, user, pass)
      conn.createStatement().executeUpdate("DELETE FROM "+ table + " WHERE dt = '" +configDate.getCurrentDate()+"'")
    }catch{
      case e: Exception => e.printStackTrace()
    }
  }
}
