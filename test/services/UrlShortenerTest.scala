package services


import org.scalatestplus.play.PlaySpec
import com.typesafe.config.ConfigFactory
import play.api.Configuration
import play.inject.guice.GuiceApplicationBuilder

import java.io.File



class UrlShortenerTest extends PlaySpec {

  val urlCreator = new UrlCreator
//  val ff= new File("test/conf/application.conf")
//  val  guiceApplicationBuilder = new GuiceApplicationBuilder()
//    .withConfigLoader(x=> ConfigFactory.load("test/conf/application.conf"))
//  val app= guiceApplicationBuilder.build
  val testConfiguration: Configuration= Configuration(ConfigFactory.load("test/conf/application.conf"))

  val inMemoryDataStore= new InMemoryDataStore(testConfiguration,new UrlCreator)
  val urlShortenerService= new UrlShortenerService(inMemoryDataStore)

  "standard functionality of creation " must{
    "should work" in{
      val res=urlShortenerService.shorten("http://www.abc.com/aaa/bbb/ccc")
      res.isDefined mustBe true
      res.get.shortUrl.substring(0,20) mustBe "http://RickMorty.com"
    }
  }

  "standard functionality of creation " must{
    "should has length 28 with http" in{
      val res=urlShortenerService.shorten("http://www.abc.com/aaa/bbb/ccc/llll")
      res.isDefined mustBe true
      res.get.shortUrl.length mustBe 28
    }
  }

  "standard functionality of creation " must{
    "should work with https and len 29" in{
      val res=urlShortenerService.shorten("https://www.abc.com/aaa/bbb/ccc/llll")
      res.isDefined mustBe true
      res.get.shortUrl.substring(0,5) mustBe "https"
      res.get.shortUrl.length mustBe 29
    }
  }

  "standard functionality of creation " must{
    "should not work with invalid url http" in{
      val res=urlShortenerService.shorten("htvps://www.abc.com/aaa/bbb/ccc/llll")
      res.isDefined mustBe false
    }
  }

  "standard functionality of creation " must{
    "should not work with invalid url missing slashes" in{
      val res=urlShortenerService.shorten("http:||www.abc.com/aaa/bbb/ccc")
      res.isDefined mustBe false
    }
  }

  "standard functionality of fetching " must{
    "should work" in{
      val res1=urlShortenerService.shorten("https://www.abc.com/aaa/bbb/ccc")
      val res2=urlShortenerService.shorten("https://www.abc.com/eee/bbb/ccc")
      val loaded=urlShortenerService.get("https://www.abc.com/eee/bbb/ccc")
      loaded.isDefined mustBe true
    }
  }

}
