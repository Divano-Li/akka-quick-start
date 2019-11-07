package akka

import akka.actor.{ ActorSystem, Actor, ActorRef, Props, PoisonPill }
import language.postfixOps
import scala.concurrent.duration._

case object Ping
case object Pong
case class PrintScreen(message: Int)

class Pinger extends Actor {

  var countDown = 100
  var i:Int = 100

  def printScreen(message: Int): Unit = {
    i = i +1
    println(message + " " + i)
  }

  def receive = {
    case Pong =>  {
      println(s"${self.path}  $sender() received pong, count down $countDown")

      if (countDown > 0) {
        countDown -= 1
        sender() ! Ping
      } else {
        sender() ! PoisonPill
        self ! PoisonPill
      }
    }
    case PrintScreen(message) => {
      println(s"$self   $sender()")
      //self ! printScreen(message)   当使用self 发送消息的话 不会发生deadLetter， 使用sender 发送消息的时，由于传入进来的sender
                                    //为system.deadLetter,
      sender() ! printScreen(222)
    }
    case _ => {
      println(s"$self ***************  $sender()")
      println("__________________")
    }
  }
}

class Ponger(pinger: ActorRef) extends Actor {
  def receive = {
    case Ping ⇒
      println(s"${self.path}  received ping   $sender()")
      pinger ! Pong
  }
}
object Example extends App {
  val system = ActorSystem("pingpong")

  val pinger = system.actorOf(Props[Pinger], "pinger")

  val ponger = system.actorOf(Props(classOf[Ponger], pinger), "ponger")

  import system.dispatcher
  system.scheduler.scheduleOnce(500 millis) {
    // ponger ! Ping
     pinger ! PrintScreen(111)   // 看下 ！方法的定义, 若sender 为 null,  则自动发送的方向,需要明白的是如何定义消息的发送者 和 接受者
                                 // 如 xxx ! message1  这样的形式，那么的话， xxx 就是消息的接受者，给xxx
                                 // 发送了一个message1 的消息，那么消息的发送者是谁呢？ 我们可以看下！ 方法的定义
                                 // 当一个消息在Actor 外面被发送，那么的话 发送者就是 null 被转换为 system.deadLetter,
  }
}

