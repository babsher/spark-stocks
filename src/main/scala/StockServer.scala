import java.io._
import java.net._

import com.google.common.base.Joiner

import scala.collection.JavaConversions

/**
 * Creates a simple socket server that sends random symbol values.
 * The program takes the following arguments:
 *
 * [port] [Symbol Name]:[Initial Value]
 *
 * The program will accept and serve data for as many symbols as it is given.
 *
 * The wire format is space separated values separated by a comma terminated by
 * a new line. Each line will contain values for all symbols given.
 *
 * [Symbol Name] [Price] [Volume], ...
 */
object StockServer extends App {

  case class Stock(name: String, rv: RandomStock)

  var port = args(0).toInt
  val stocks = args.toIterable.drop(1)
    .map(a => {
      val s = a.split(":")
      val value = s(1).toDouble
      Stock(s(0), new RandomStock(value, 1.5))
    }).toSeq

  class Handler(sock: Socket) extends Runnable {
    def line(): String = {
      val newValues = stocks.map(stock =>
        Joiner.on(" ").join(stock.name, stock.rv.getPrice, stock.rv.getVolume))
      Joiner.on(",").join(JavaConversions.asJavaCollection(newValues))
    }

    override def run(): Unit = {
      val out = new PrintStream(sock.getOutputStream)
      while (true) {
        out.println(line())
        Thread.sleep(250)
      }
    }
  }

  val server = new ServerSocket(port)
  println("Server running waiting for a connection...")
  while (true) {
    val s = server.accept()
    println("Got connection")
    new Thread(new Handler(s)).start()
  }
}
