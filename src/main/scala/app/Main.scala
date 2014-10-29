package app

import java.net.InetSocketAddress

import com.twitter.finagle._
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.http.service.RoutingService
import com.twitter.finagle.http.{Http, Request, Response, RichHttp}
import com.twitter.util._
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.handler.codec.http.DefaultHttpResponse
import org.jboss.netty.handler.codec.http.HttpResponseStatus.{OK, _}
import org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1
import org.jboss.netty.util.CharsetUtil.UTF_8

object Main {
  def main(args: Array[String]): Unit = {
    ServerBuilder()
      .codec(RichHttp[Request](Http()))
      .bindTo(new InetSocketAddress(8080))
      .name("app")
      .keepAlive(true)
      .build(exceptionFilter andThen routingService)
  }

  val routingService =
    RoutingService.byPath {
      case "/" => new HelloService
    }

  val exceptionFilter = new SimpleFilter[Request, Response] {
    def apply(request: Request, service: Service[Request, Response]) = {
      service(request) handle {
        case e: Throwable => {
          println(e)
          Response(new DefaultHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR))
        }
      }
    }
  }

  class HelloService extends Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      val response = new DefaultHttpResponse(HTTP_1_1, OK)
      response.setContent(ChannelBuffers.copiedBuffer("Hello!", UTF_8))
      Future.value(Response(response))
    }
  }

  class NotFoundService extends Service[Request, Response] {
    def apply(request: Request): Future[Response] = {
      Future.value(Response(new DefaultHttpResponse(HTTP_1_1, NOT_FOUND)))
    }
  }
}
