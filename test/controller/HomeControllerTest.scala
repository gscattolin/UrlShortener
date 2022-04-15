package controller


import com.typesafe.config.ConfigFactory
import controllers.HomeController
import org.scalatestplus.play._
import play.api.Configuration
import play.api.libs.json.{JsString, Json}
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import services.{InMemoryDataStore, UrlCreator, UrlShortenerService}


class HomeControllerTest extends PlaySpec with Results{

  val urlCreator = new UrlCreator
  val testConfiguration: Configuration= Configuration(ConfigFactory.load("application-test"))

  val inMemoryDataStore= new InMemoryDataStore(testConfiguration,new UrlCreator)
  val urlShortenerService= new UrlShortenerService(inMemoryDataStore)
  val controller = new HomeController(Helpers.stubControllerComponents(),urlShortenerService)


  "controller post create  " must{
    "should result status OK" in {
      val method = controller.createUrl().apply(FakeRequest(POST, "/url").withJsonBody(Json.parse(""" {"url" : "http://aaa.bbb.com/aaa/bbb"} """)))
      status(method) mustBe OK
    }
  }

  "controller post create  " must{
    "should result json body should contain RickMortyTest.com and length 32" in {
      val method = controller.createUrl().apply(FakeRequest(POST, "/url").withJsonBody(Json.parse(""" {"url" : "http://aaa.bbb.com/aaa/ccc"} """)))
      val res= contentAsJson(method).asInstanceOf[JsString].value
      res.substring(0,24) mustBe "http://RickMortyTest.com"
      res.length mustBe 32
    }
  }

  "controller get method  " must{
    "should result json body should contain RickMortyTest.com for the previous" in {
      val json=""" {"url" : "http://aaa.bbb.com/aaa/kkk"} """
      val created=controller.createUrl().apply(FakeRequest(POST, "/url").withJsonBody(Json.parse(json)))
      val createUrl= contentAsJson(created).asInstanceOf[JsString].value
      val fetched=controller.createUrl().apply(FakeRequest(GET, "/url").withJsonBody(Json.parse(json)))
      val fetchedUrl= contentAsJson(fetched).asInstanceOf[JsString].value
      fetchedUrl.substring(0,24) mustBe "http://RickMortyTest.com"
      fetchedUrl.length mustBe 32
      fetchedUrl mustBe createUrl
    }
  }

  "controller post create  " must{
    "should result json body should contain Invalid Url for wrong url" in {
      val method = controller.createUrl().apply(FakeRequest(POST, "/url").withJsonBody(Json.parse(""" {"url" : "http:!!aaa.bbb.com/aaa/ccc"} """)))
      val res= contentAsJson(method).asInstanceOf[JsString].value
      res mustBe "Invalid Url"
    }
  }

}
