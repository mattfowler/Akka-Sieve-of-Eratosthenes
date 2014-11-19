import akka.actor.{ActorRefFactory, Props, ActorSystem}
import akka.routing.RoundRobinRouter
import message.Calculate
import worker.{EratosthenesWorker, EratosthenesCompletionListener, EratosthenesMaster}


object PrimeCalculatorApp  {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("PrimeSystem")

    val listener = system.actorOf(Props[EratosthenesCompletionListener], name = "listener")

    val factory = (actorRefFactory: ActorRefFactory, workers:Int) =>
      actorRefFactory.actorOf(Props[EratosthenesWorker].withRouter(RoundRobinRouter(workers)))
    val master = system.actorOf(Props(new EratosthenesMaster(factory, 4, 100000, listener)), name = "master")

    master ! Calculate
  }

}
