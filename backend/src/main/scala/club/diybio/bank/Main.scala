package club.diybio.bank

import akka.actor.{ActorSystem, _}
import com.typesafe.config.Config

/**
 * Main class of plasmid bank application
 */
object Main extends App
{
  implicit val system = ActorSystem()
  sys.addShutdownHook(system.shutdown())

  val config: Config = system.settings.config
  var main:ActorRef = system.actorOf(Props[MainActor])
  main ! AppMessages.Start(config)

}