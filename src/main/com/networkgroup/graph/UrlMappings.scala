package com.networkgroup.graph

import com.google.gson.{JsonArray, JsonObject}
import com.networkgroup.graph.Constants.{KEY_CATEGORY, KEY_COUNT, KEY_DATA}
import spark.Spark._
import spark._
import spark.template.mustache.MustacheTemplateEngine

import scala.util.Random
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

    get("/", new TemplateViewRoute {
      override def handle(request: Request, response: Response): ModelAndView = {
        new ModelAndView(Map.empty, "result.html.mustache")
      }
    }, new MustacheTemplateEngine("./"))

    get("/data", new Route {
      override def handle(request: Request, response: Response): AnyRef = {
        response.`type`("application/json")
        socketMan.getGraphData.toString
      }
    })

  }

}
