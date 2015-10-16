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

  var mean = start
  var current = start
  val dist = new NormalDistribution(0, 1)

  def next: Double = {
    val wt = dist.sample() / Math.sqrt(1)
    current = current + drift * (mean - current) + vol * wt
    current
  }
}
