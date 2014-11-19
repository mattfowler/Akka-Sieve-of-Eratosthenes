package worker


import java.util.concurrent.TimeUnit

import akka.actor.{Props, ActorRefFactory, ActorSystem}
import akka.routing.RoundRobinRouter
import akka.testkit.{TestProbe, TestKit, TestActorRef}
import akka.util.Timeout
import message.{AllPrimesResult, CalculatePrimesMessage, Calculate}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpecLike, Matchers}
import akka.pattern.ask

@RunWith(classOf[JUnitRunner])
class EratosthenesMasterTest extends TestKit(ActorSystem()) with FlatSpecLike with Matchers {

  implicit val timeout = Timeout(3, TimeUnit.SECONDS)

  it should "chunk up results in equal increments"  in {
    val childActor = TestProbe()

    val factory = (actorRefFactory: ActorRefFactory, workers:Int) => childActor.ref

    val eratosthenesMasterActor = TestActorRef(new EratosthenesMaster(factory, 4, 200, testActor))

    eratosthenesMasterActor ?  Calculate

    childActor.expectMsg(CalculatePrimesMessage(2, 50))
    childActor.expectMsg(CalculatePrimesMessage(50, 100))
    childActor.expectMsg(CalculatePrimesMessage(100, 150))
    childActor.expectMsg(CalculatePrimesMessage(150, 200))

    childActor.expectNoMsg()
  }

  it should "combine results appropriately" in {
    val factory = (actorRefFactory: ActorRefFactory, workers:Int) =>
      actorRefFactory.actorOf(Props[EratosthenesWorker].withRouter(RoundRobinRouter(workers)))
    val eratosthenesMasterActor = TestActorRef(new EratosthenesMaster(factory, 4, 100, testActor))

    eratosthenesMasterActor ?  Calculate

    expectMsgPF() {
      case AllPrimesResult(primes, _) => primes == List(2, 3, 5, 7, 11, 13, 17, 19, 23, 53, 59, 61, 67, 71, 73, 29, 31, 37, 41, 43, 47, 79, 83, 89, 97)
    }
  }

  it should "notify the listener when all calculations are done" in {
    val factory = (actorRefFactory: ActorRefFactory, workers:Int) =>
      actorRefFactory.actorOf(Props[EratosthenesWorker].withRouter(RoundRobinRouter(workers)))
    val listener = TestProbe()
    val eratosthenesMasterActor = TestActorRef(new EratosthenesMaster(factory, 4, 100, listener.ref))

    eratosthenesMasterActor ?  Calculate

    listener.expectMsgType[AllPrimesResult]
  }

}
