import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.DStream


object StockStreaming {

  case class Stock(name: String, value: Double, vol: Int)

  def connectToStream(ssc: StreamingContext, host: String, port: Int): DStream[Seq[Stock]] = {
    val lines = ssc.socketTextStream(host, port)

    lines.map(_.split(",").toSeq).
      map(_.map(u => {
      val stock = u.split(" ")
      Stock(stock(0), stock(1).toDouble, stock(2).toInt)
    }))
  }

  def vwap(ssc: StreamingContext): DStream[(String, Double)] = {
    val stocks = connectToStream(ssc, "localhost", 9999)

    stocks.window(Seconds(30), Seconds(15)).
      flatMap(_.map(u => {
        (u.name, (u.vol * u.value, u.vol))
      }))
      .reduceByKey((e1, e2) => (e1._1 + e2._1, e1._2 + e2._2))
      .map{case (stock, (price, vol)) => (stock, price / vol)}
  }
}
