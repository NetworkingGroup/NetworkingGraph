package com.networkgroup.graph

import freemarker.template.Configuration
import spark.Spark._
import spark._
import spark.template.freemarker.FreeMarkerEngine

import scala.io.StdIn.readLine

/**
  * Class to define url patterns and their expected interactions.
  */
object UrlMappings {

  def main(args: Array[String]): Unit = {

    val serverPort = readLine("What port to run server on:").toInt
    val dataPort = readLine("What port to listen on for data update:").toInt

    val socketMan = new SocketMan(dataPort)
    val dataThread = new Thread(socketMan)
    dataThread.start()

    port(serverPort)
    Spark.staticFileLocation("/")
    val config = new Configuration
    config.setClassForTemplateLoading(UrlMappings.getClass, "/")

    get("/", new TemplateViewRoute {
      override def handle(request: Request, response: Response): ModelAndView = {
        new ModelAndView(Map.empty, "result.html.ftl")
      }
    }, new FreeMarkerEngine(config))

    get("/data", new Route {
      override def handle(request: Request, response: Response): AnyRef = {
        response.`type`("application/json")
        socketMan.getGraphData.toString
      }
    })

  }

}
