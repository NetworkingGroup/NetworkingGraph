package com.networkgroup.graph

import com.google.gson.{JsonArray, JsonObject}
import spark.Spark._
import spark._
import spark.template.mustache.MustacheTemplateEngine

import scala.util.Random

/**
  * Class to define url patterns and their expected interactions.
  */
object UrlMappings {

  private val KEY_DATA = "Data"
  private val KEY_CATEGORY = "Category"
  private val KEY_COUNT = "Count"

  def main(args: Array[String]): Unit = {

    Spark.staticFileLocation("/")
    port(7001)

    val categories = List(
      "Games",
      "Cool Stuff",
      "Weird Stuff",
      "Comedy",
      "Television",
      "Console",
      "Most Popular",
      "Matches",
      "Moxie",
      "Pi",
      "RHO"
    )

    get("/", new TemplateViewRoute {
      override def handle(request: Request, response: Response): ModelAndView = {
        new ModelAndView(Map.empty, "result.html.mustache")
      }
    }, new MustacheTemplateEngine("./"))

    get("/data", new Route {
      override def handle(request: Request, response: Response): AnyRef = {
        val shuffled = Random.shuffle(categories)
        val sub = shuffled.slice(0, Random.nextInt(shuffled.size - 2) + 2)
        val json = new JsonObject
        val array = new JsonArray

        sub.foreach(item => {
          val temp = new JsonObject
          temp.addProperty(KEY_CATEGORY, item)
          temp.addProperty(KEY_COUNT, Random.nextInt(500))
          array.add(temp)
        })
        json.add(KEY_DATA, array)

        response.`type`("application/json")
        json.toString
      }
    })

  }

}
