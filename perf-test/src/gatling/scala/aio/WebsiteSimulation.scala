package aio

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class WebsiteSimulation extends Simulation {
  val requestsPerSecond = System.getProperty("rps", "100").toDouble
  val port = System.getProperty("port", "8080")

  val httpConf = http
    .baseURL("http://localhost:" + port)
    .acceptHeader("text/html")
    .shareConnections

  val scn = scenario("WebsiteSimulation")
    .exec(http("simple_request").get("/"))

  setUp(
    scn.inject(
      constantUsersPerSec(requestsPerSecond) during (30 seconds)
    )
    .protocols(httpConf)
  )

}
