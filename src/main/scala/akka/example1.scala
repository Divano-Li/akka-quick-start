package akka

import akka.actor.{Actor, ActorSystem, Props}


case class MyValueClass(v: Int) extends AnyVal

class ValueActor(value: MyValueClass) extends Actor {
  def receive = {
    case multiplier: Long ⇒ sender() ! (value.v * multiplier)
  }
}

class DefaultValueActor(a: Int, b: Int = 5) extends Actor {
  def receive = {
    case x: Int ⇒ sender() ! ((a + x) * b)
  }
}

class DefaultValueActor2(b: Int = 5) extends Actor {
  def receive = {
    case x: Int ⇒ sender() ! (x * b)
  }
}

object  example1 extends  App {
  val system = ActorSystem("Example1")

 // val valueClassProp = Props(classOf[ValueActor], MyValueClass(5)) // Unsupported
    val valueClassProp1 = Props(new ValueActor(new MyValueClass(1)))
 // val defaultValueProp2 = Props[DefaultValueActor2] // Unsupported
 // val defaultValueProp3 = Props(classOf[DefaultValueActor2]) // Unsupported
 // val defaultValueProp1 = Props(classOf[DefaultValueActor], 2) // Unsupported

  //system.actorOf(defaultValueProp2)
}
