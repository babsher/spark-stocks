import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.commons.math3.random.{MersenneTwister, RandomGenerator}

class RandomVariable(var lastValue: Double, val sd: Double) {

  val rand: RandomGenerator = new MersenneTwister()

  val dist = new NormalDistribution(rand, 0, sd)
  val volume = new NormalDistribution(rand, 100, 50)

  def getPrice: Double = {
    lastValue = (math rint (lastValue + dist.sample()) * 100) / 100
    lastValue
  }

  def getVolume: Integer = math.rint(math.abs(volume.sample)).toInt
}
