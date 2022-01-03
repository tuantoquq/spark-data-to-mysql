package com.admicro.process

import com.admicro.base.BaseProcess
import com.admicro.dao.hdfs.CpaLogDao
import com.admicro.factory.MysqlProcessor
import com.admicro.model.CpaAnalyticModel.LogFull
import com.admicro.utils.config.{ConfigInfo, CustomLog}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

import java.util.{Calendar, Properties}

class CpaAnalyticProcess(spark: SparkSession) extends BaseProcess(spark){
  var cpaData: Dataset[LogFull] = _
  var cpaLogsDF: DataFrame = _
  val connectProperties = new Properties()


  override def run(dateRun: String): Unit = {
    //get data from hdfs
    dataInput(dateRun)
    //process data
    process(dateRun)
    //save data into database
    saveData(dateRun)
  }

  override def dataInput(dateRun: String): Unit ={
    cpaData = cpaDataInput(dateRun)
    CustomLog.printLog("Cpa Logs Data: ")
    cpaData.show(10)
  }

  override def process(dateRun: String): Unit = {
    connectProperties.setProperty("user",ConfigInfo.mysqlUserName)
    connectProperties.setProperty("password",ConfigInfo.mysqlPassWord)
    connectProperties.setProperty("driver",ConfigInfo.mysqlDriver)
    connectProperties.setProperty("truncate","true")
    MysqlProcessor.deleteData(ConfigInfo.mysqlTable,ConfigInfo.mysqlUrl,ConfigInfo.mysqlUserName,
      ConfigInfo.mysqlPassWord,ConfigInfo.mysqlDriver)
    cpaLogsDF = cpaData.toDF()
  }

  override def saveData(dateRun: String): Unit = {
//    dataFrame.coalesce(1).write
//      .mode("overwrite")
//      .option("header","true")
//      .csv(ConfigInfo.hdfsCpaOutput)
    if(cpaLogsDF != null){
      MysqlProcessor.save(cpaLogsDF,ConfigInfo.mysqlTable,ConfigInfo.mysqlUrl,connectProperties)
    }else{
      CustomLog.printLog("Data to insert is empty!")
    }
  }

  def cpaDataInput(dateRun: String): Dataset[LogFull] ={
    val cpaLogDao = new CpaLogDao(spark)
    val input = ConfigInfo.hdfsCpaInput + dateRun
    val cpaLogs: Dataset[LogFull] = cpaLogDao.getData(input)
    cpaLogs
  }

}
