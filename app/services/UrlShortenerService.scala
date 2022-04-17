package services

import com.google.inject.ImplementedBy
import models.{Stats, UrlData}
import play.api.Logging

import javax.inject.{Inject, Singleton}
import scala.util.matching.Regex

@ImplementedBy(classOf[UrlShortenerService])
trait UrlShortener {

  def shorten(urlOriginal:String): Option[UrlData]

  def get(code: String): Option[UrlData]

  def getOriginalUrl(shortUrl:String): Option[UrlData]

  def stats(urlOriginal:String): Option[Stats]

}

class UrlShortenerService @Inject() (dataStore:dataUrlsStore) extends UrlShortener with Logging{

  private val urlRegex: Regex = """^(https?|http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]""".r

  private def isValidUrl(urlOriginal:String): Boolean = {
    urlRegex.unapplySeq(urlOriginal).isDefined
  }


  private def getOrCreateUrl(urlOriginal: String): UrlData={
    val urlData=dataStore.load(urlOriginal)
    if (urlData.isDefined)
      urlData.get
    else
      dataStore.save(urlOriginal)
  }

  override def shorten(urlOriginal: String): Option[UrlData] = {
    if (!isValidUrl(urlOriginal)){
      logger.error(s"Url defined is not valid $urlOriginal")
      None
    }else{
      Some(getOrCreateUrl(urlOriginal))
    }
  }

  override def get(shortUrl: String): Option[UrlData] = {
    if (!isValidUrl(shortUrl)){
      logger.error(s"Url is not valid $shortUrl")
      None
    }else {
      dataStore.load(shortUrl)
    }
  }

  override def stats(urlOriginal: String): Option[Stats] = ???

  override def getOriginalUrl(shortUrl: String):  Option[UrlData] = {
    if (!isValidUrl(shortUrl)){
      logger.error(s"Url is not valid $shortUrl")
      None
    }else {
      dataStore.loadOriginal(shortUrl)
    }
  }
}
