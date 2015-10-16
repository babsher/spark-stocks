import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.commons.math3.random.{MersenneTwister, RandomGenerator}

class RandomStock(var lastValue: Double, val sd: Double) {

  val walk = new GeometricRandomWalk(lastValue, .5, sd)
  val rand: RandomGenerator = new MersenneTwister()
  val volume = new NormalDistribution(rand, 100, 50)

  def getPrice: Double = walk.next

  def getVolume: Integer = math.rint(math.abs(volume.sample)).toInt
}
