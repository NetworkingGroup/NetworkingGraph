package com.networkgroup.graph

import java.net.{DatagramPacket, DatagramSocket}
import java.nio.ByteBuffer

import com.google.gson.{JsonArray, JsonObject}
import com.networkgroup.graph.Constants._

import scala.collection.mutable.{Map => MutMap}

/**
  * Class to implement socket to listen for data packets.
  *
  * @param port The port to listen on.
  */
class SocketMan(port: Int) extends Runnable {

  private val socket = new DatagramSocket(port)

  private val readPacket = new DatagramPacket(new Array[Byte](PACKET_DATA_SIZE), 0, PACKET_DATA_SIZE)
  private val dataMap = MutMap[Int, Long]()

  override def run(): Unit = {

    while (true) {
      socket.receive(readPacket)

      dataMap.synchronized {
        val buffer = ByteBuffer.wrap(readPacket.getData)
        dataMap += buffer.get().toInt -> buffer.getLong
      }
    }
  }

  /**
    * Method to get current graph data.
    *
    * @return The graph data.
    */
  def getGraphData: JsonObject = {
    val outer = new JsonObject
    val array = new JsonArray

    dataMap.synchronized {
      dataMap.foreach(pair => {
        val inner = new JsonObject
        inner.addProperty(KEY_CATEGORY, VALUES.get(pair._1).get)
        inner.addProperty(KEY_COUNT, pair._2)
        array.add(inner)
      })
    }
    outer.add(KEY_DATA, array)
    outer
  }

}

