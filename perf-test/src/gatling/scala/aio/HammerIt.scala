package aio

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class HammerIt extends Simulation {
  val requestsPerSecond = System.getProperty("rps", "100").toDouble
  val duration = System.getProperty("duration", "30").toDouble
  val port = System.getProperty("port", "8500")

  val httpConf = http
    .baseURL("http://localhost:" + port)
    .acceptHeader("text/html")
    .shareConnections

  val scn = scenario("HammerIt")
    .exec(http("simple_request").get("/"))

  setUp(
    scn.inject(
      constantUsersPerSec(requestsPerSecond) during (duration seconds)
    )
    .protocols(httpConf)
  )

}
