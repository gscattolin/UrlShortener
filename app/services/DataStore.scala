package services

import com.google.inject.ImplementedBy
import org.joda.time.DateTime
import models.UrlData
import play.api.Configuration

import javax.inject.{Inject, Singleton}
import scala.collection.mutable.HashMap
import scala.util.matching.Regex

@ImplementedBy(classOf[InMemoryDataStore])
trait dataUrlsStore{

  def save(urlOriginal:String):UrlData
  def load(urlOriginal:String):Option[UrlData]
  def loadOriginal(shortUrl:String):Option[UrlData]
  def purge():Option[Int]
}

@Singleton
class InMemoryDataStore @Inject() (config:Configuration,urlCreator:UrlCreator) extends  dataUrlsStore {

  private val urlRegex: Regex = """^(http|https):\/\/""".r
  private val inMemory= new HashMap[String,UrlData]
  private val UrlDomain=config.get[String]("UrlDomain")

  private def  createUrlByCode(prefix:String,code:String):String=s"$prefix$UrlDomain/$code"


  override def save(urlOriginal:String ): UrlData = {
    val hasValue=urlCreator.createShortCode(urlOriginal)
    val prefix=urlRegex.findFirstIn(urlOriginal).get
    val urlData= UrlData(urlOriginal,createUrlByCode(prefix,hasValue),DateTime.now)
    inMemory += hasValue ->urlData
    urlData
  }

  override def load(urlOriginal: String): Option[UrlData] = {
    val code=urlCreator.createShortCode(urlOriginal)
    inMemory.get(code)
  }

  def loadOriginal(shortUrl:String):Option[UrlData] = {
    val prefix=urlRegex.findFirstIn(shortUrl).get
    val startingPos=prefix.length+UrlDomain.length+1
    var urlData:Option[UrlData]=None
    if (startingPos<shortUrl.length){
      val code=shortUrl.substring(startingPos)
      if (inMemory.contains(code)){
        urlData=inMemory.get(code)
      }
    }
    urlData
  }

  override def purge(): Option[Int] =  ???
}