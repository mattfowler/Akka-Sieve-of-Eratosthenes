package worker

import akka.actor.ActorSystem
import akka.testkit.TestActorRef
import exception.InvalidInputException
import message.{CalculatePrimesMessage, PrimesResult}
import org.scalatest.{FlatSpec, Matchers}

class EratosthenesWorkerTest extends FlatSpec with Matchers {

  implicit val system = ActorSystem.create()
  val eratosthenesActor = TestActorRef[EratosthenesWorker]

  it should "calculate primes between two numbers" in {
    eratosthenesActor.receive(CalculatePrimesMessage(2, 2)) should equal(PrimesResult(List(2, 2)))
    eratosthenesActor.receive(CalculatePrimesMessage(2, 10)) should equal(PrimesResult(List(2, 3, 5, 7)))
    eratosthenesActor.receive(CalculatePrimesMessage(2, 10)) should equal(PrimesResult(List(23, 29, 31, 37)))
  }

  it should "throw an error if start is not less than end or numbers are negative" in {

    intercept[InvalidInputException] {
      eratosthenesActor.receive(CalculatePrimesMessage(8, 5))
    }

    intercept[InvalidInputException] {
      eratosthenesActor.receive(CalculatePrimesMessage(-8, 1))
    }
  }

}
