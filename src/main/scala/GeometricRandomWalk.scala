import org.apache.commons.math3.distribution.NormalDistribution

/**
 *
 * https://en.wikipedia.org/wiki/Geometric_Brownian_motion
 *
 * @param start
 * @param drift
 * @param vol
 */
class GeometricRandomWalk(val start: Double, val drift: Double, val vol: Double) {

  var current = start
  val dist = new NormalDistribution(0, 1)

  def next: Double = {
    val ex: Double = drift - (vol * vol) / 2
    val wt: Double = dist.sample()
    current = current * Math.exp(ex + vol * wt)
  }
}
