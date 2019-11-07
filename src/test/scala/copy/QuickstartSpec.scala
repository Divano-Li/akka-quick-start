package copy

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import copy.Greeter.{Greet, WhoToGreet}
import copy.Printer.Greeting
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import scala.concurrent.duration._

class QuickstartSpec (_system:ActorSystem) extends TestKit(_system) with Matchers with FlatSpecLike with BeforeAndAfterAll{

  def this() = this(ActorSystem("QuickStart"))

  override def afterAll():Unit = {
    shutdown(system)
  }
  //#first-test
  //#specification-example
  "A Greeter Actor" should "pass on a greeting message when instructed to" in {
    //#specification-example
    val testProbe = TestProbe()
    val helloGreetingMessage = "hello"
    val helloGreeter = system.actorOf(Greeter.props(helloGreetingMessage, testProbe.ref))
    val greetPerson = "Akka"
    helloGreeter ! WhoToGreet(greetPerson)
    helloGreeter ! Greet
    testProbe.expectMsg(500 millis, Greeting(s"$helloGreetingMessage, $greetPerson"))
  }
  //#first-test
}
