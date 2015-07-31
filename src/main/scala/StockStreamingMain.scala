import org.apache.spark._
import org.apache.spark.streaming._

object StockStreamingMain extends App {
  val conf = new SparkConf()
    .setMaster("local[2]")
    .setAppName("Stocks")
    .set("spark.executor.memory", "1g")
  val sc = new SparkContext(conf)
  val ssc = new StreamingContext(sc, Seconds(1))

  val vwap = StockStreaming.vwap(ssc)
  vwap.print()

  ssc.start()
  ssc.awaitTermination()
}
