package club.diybio.bank

import akka.actor.{ActorSystem, _}

/**
 * For running as kernel
 */
object Main extends App
{
  implicit val system = ActorSystem()

  var main:ActorRef = null

  def startup(): Unit = {

    main =  system.actorOf(Props[MainActor])
    main ! AppMessages.Start(1234)

  }

  def shutdown(): Unit = {
    system.shutdown()
  }

  startup()


}
