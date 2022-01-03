package com.admicro.model

import java.sql.Date

object CpaAnalyticModel{
  case class LogCpa(
                   var timeLog: String,
                   var transId: String,
                   var success: Int,
                   var domain: String,
                   var path: String,
                   var cpa_value: String,
                   var srcId: Int,
                   var reqId: String,
                   var step: String,
                   var guid: String,
                   var dmGuid: String,
                   var sourceDomain: String,
                   var ip: String,
                   var ipLong: String
                   )
  case class LogFull(
                      var dt: Date,
                      var timeLog: String,
                      var transId: String,
                      var success: Int,
                      var domain: String,
                      var path: String,
                      var cpaId: String,
                      var eventId: String,
                      var cpaValue: String,
                      var cpaOrderId: String,
                      var srcId: Int,
                      var reqId: String,
                      var step: String,
                      var guid: String,
                      var DmGuid: String,
                      var sourceDomain: String,
                      var ip: String,
                      var ipLong: Long
                    )
  object CpaAnalyticInput{
    def apply(
              dt: Date,
              timeLog: String,
              transId: String,
              success: Int,
              domain: String,
              path: String,
              cpaId: String,
              eventId: String,
              cpaValue: String,
              cpaOrderId: String,
              srcId: Int,
              reqId: String,
              step: String,
              guid: String,
              dmGuid: String,
              sourceDomain: String,
              ip: String,
              ipLong: Long
             ): LogFull={
    val c = LogFull(dt,timeLog,transId,success,domain,path,cpaId,eventId,cpaValue,cpaOrderId,srcId,reqId,step,guid,dmGuid,sourceDomain,ip,ipLong)
      c
    }
  }
}
