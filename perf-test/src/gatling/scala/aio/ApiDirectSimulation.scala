package aio

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ApiDirectSimulation extends Simulation {
  val requestsPerSecond = System.getProperty("rps", "100").toDouble

  val httpConf = http
    .baseURL("http://localhost:8500")
    .acceptHeader("text/html")
    .shareConnections

  val scn = scenario("ApiDirect")
    .exec(http("simple_request").get("/"))

  setUp(
    scn.inject(
      constantUsersPerSec(requestsPerSecond) during (30 seconds)
    )
    .protocols(httpConf)
  )

}
