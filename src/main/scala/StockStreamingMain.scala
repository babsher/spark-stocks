import org.apache.spark._
import org.apache.spark.sql.{SaveMode, SQLContext}
import org.apache.spark.streaming._

object StockStreamingMain extends App {
  val conf = new SparkConf()
    .setMaster("local[2]")
    .setAppName("Stocks")
    .set("spark.executor.memory", "1g")
  val sc = new SparkContext(conf)
  val ssc = new StreamingContext(sc, Seconds(1))

  val vwapStream = StockStreaming.vwap(ssc)

  vwapStream.foreachRDD(rdd=> {
    // Get the singleton instance of SQLContext
    val sqlContext = SQLContext.getOrCreate(rdd.sparkContext)
    import sqlContext.implicits._

    rdd.toDF().registerTempTable("vwap")
    println(rdd.count())
  })

  ssc.start()
  ssc.awaitTermination()
}
