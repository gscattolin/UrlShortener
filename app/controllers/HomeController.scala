package controllers

import com.google.inject.Inject
import models.UrlData
import play.api.{Logger, Logging}
import play.api.libs.json.{JsResult, JsSuccess, JsValue, Json}
import play.api.mvc._
import services.UrlShortener



class HomeController @Inject()(cc: ControllerComponents,urlShortener: UrlShortener) extends AbstractController(cc) with Logging {

  private final case class urlPost(url:String)
  protected val Log: Logger = Logger(this.getClass)


  private def processReply(urlData:Option[UrlData]):Result={
    urlData match {
      case Some(url) => Ok(Json.toJson(url.shortUrl))
      case None => Ok(Json.toJson("Invalid Url"))
    }
  }


  def getUrl(urlOriginal:String): Action[AnyContent] = Action{
    Log.debug(s"Looking for $urlOriginal")
    processReply(urlShortener.get(urlOriginal))
  }

  def getOriginalUrl(shortUrl:String): Action[AnyContent] = Action{
    Log.debug(s"Looking for original Url from $shortUrl")
    processReply(urlShortener.get(shortUrl))
  }

  def createUrl(): Action[AnyContent] =Action { request =>
    def reads(json: JsValue): JsResult[urlPost] = {
      val v1 = (json \ "url").as[String]
      JsSuccess(urlPost(v1))
    }
    Log.debug("receive post")
    val json = request.body.asJson.get
    val urlValue = json.as[urlPost](reads)
    processReply(urlShortener.shorten(urlValue.url))
  }
}
