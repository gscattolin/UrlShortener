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

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index())
  }

  private def processReply(urlData:Option[UrlData]):Result={
    urlData match {
      case Some(url) => Ok(Json.toJson(url.shortUrl))
      case None => Ok(Json.toJson("Invalid Url"))
    }
  }


  def getUrl(urlOriginal:String): Action[AnyContent] = Action{
    Log.info(s"Looking for $urlOriginal")
    processReply(urlShortener.get(urlOriginal))
  }

  def createUrl(): Action[AnyContent] =Action { request =>
    def reads(json: JsValue): JsResult[urlPost] = {
      val v1 = (json \ "url").as[String]
      JsSuccess(urlPost(v1))
    }
    Log.info("receive post")
    val json = request.body.asJson.get
    val urlValue = json.as[urlPost](reads)
    processReply(urlShortener.shorten(urlValue.url))
  }
}
