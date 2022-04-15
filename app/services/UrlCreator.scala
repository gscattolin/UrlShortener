package services

import play.api.Logging

import java.security.MessageDigest
import java.util.Base64
import java.nio.charset.{CharacterCodingException, Charset, StandardCharsets}

trait urlCreatorStrategy{

  def createShortCode(originalUrl:String):String
}

class UrlCreator extends urlCreatorStrategy  with Logging {
  private val base64Enc: Base64.Encoder =Base64.getEncoder

  private val md5: MessageDigest =MessageDigest.getInstance("MD5")

  override def createShortCode(originalUrl: String): String = {
    logger.debug(s"Calculating short url for $originalUrl")
    base64Enc.encodeToString( md5.digest(originalUrl.getBytes(StandardCharsets.UTF_8))).substring(0,7)
  }
}
