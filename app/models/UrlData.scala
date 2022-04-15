package models

import org.joda.time.DateTime

case class UrlData (originalUrl:String, shortUrl:String,lastAccessTime:DateTime)
case class Stats(numberOfKeys:Long,OldestKey:DateTime)
