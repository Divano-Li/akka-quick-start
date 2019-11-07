package copy

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

object Greeter{
  def props(message:String,printerActor:ActorRef):Props = Props(new Greeter(message,printerActor))
  final case class WhoToGreet(who: String)
  case object Greet
}
class Greeter(message:String ,printerActor:ActorRef) extends Actor{

  import Greeter._
  import Printer._

  var greeting = ""

   def receive = {
    case WhoToGreet(who) =>
      greeting = s"$message, $who"
    case Greet =>
      printerActor ! Greeting(greeting)
  }
}
object Printer{
  def props:Props = Props[Printer]
  final case class Greeting(greeting:String)
}
class Printer extends Actor with ActorLogging {

  import Printer._

  def receive = {
    case Greeting(greeting) =>
      log.info(s"Greeting received (from  ${sender()}) :$greeting")
  }
}
object QuickStart extends App{

  import Greeter._

  val system:ActorSystem = ActorSystem("helloAkka")

  val printer:ActorRef = system.actorOf(Printer.props,"printerActor")

  val howdyGreetere:ActorRef =
    system.actorOf(Greeter.props("Howdy",printer),"howdyGreeter")

  howdyGreetere ! WhoToGreet("xulilin")
  howdyGreetere ! Greet
}


