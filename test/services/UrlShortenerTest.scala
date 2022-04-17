package services


import org.scalatestplus.play.PlaySpec
import com.typesafe.config.ConfigFactory
import play.api.Configuration


class UrlShortenerTest extends PlaySpec {

  val urlCreator = new UrlCreator
  val testConfiguration: Configuration= Configuration(ConfigFactory.load("application-test"))

  val inMemoryDataStore= new InMemoryDataStore(testConfiguration,new UrlCreator)
  val urlShortenerService= new UrlShortenerService(inMemoryDataStore)

  "standard functionality of creation " must{
    "should work" in{
      val res=urlShortenerService.shorten("http://www.abc.com/aaa/bbb/ccc")
      res.isDefined mustBe true
      res.get.shortUrl.substring(0,24) mustBe "http://RickMortyTest.com"
    }
  }

  "standard functionality of creation " must{
    "should has length 32 with http" in{
      val res=urlShortenerService.shorten("http://www.abc.com/aaa/bbb/ccc/llll")
      res.isDefined mustBe true
      res.get.shortUrl.length mustBe 32
    }
  }

  "standard functionality of creation " must{
    "should work with https and len 33" in{
      val res=urlShortenerService.shorten("https://www.abc.com/aaa/bbb/ccc/llll")
      res.isDefined mustBe true
      res.get.shortUrl.substring(0,5) mustBe "https"
      res.get.shortUrl.length mustBe 33
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

  "standard functionality of fetching reversing from short-> long url" must{
    "should work" in{
      val res1=urlShortenerService.shorten("https://www.abc.com/aaa/bbb/ccc")
      val originalUrl=urlShortenerService.getOriginalUrl( res1.get.shortUrl)
      originalUrl.isDefined mustBe true
      originalUrl.get.originalUrl mustBe "https://www.abc.com/aaa/bbb/ccc"
    }
  }


  "standard functionality of fetching reversing from short-> long url" must{
    "should not work for invalid url" in{
      val originalUrl=urlShortenerService.getOriginalUrl( "hggp://aaa.com/asas")
      originalUrl.isDefined mustBe false
    }
  }

  "standard functionality of fetching reversing from short-> long url" must{
    "should not work for not existence url" in{
      val originalUrl=urlShortenerService.getOriginalUrl( "http://aaa.com/asas/bbb")
      originalUrl.isDefined mustBe false
    }
  }

}
