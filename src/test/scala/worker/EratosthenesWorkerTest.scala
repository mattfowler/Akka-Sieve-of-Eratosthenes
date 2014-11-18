package worker


import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestActorRef}
import akka.util.Timeout
import exception.InvalidInputException
import message.{PrimesResult, CalculatePrimesMessage}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpecLike, Matchers}
import akka.pattern.ask
import scala.util.Success


@RunWith(classOf[JUnitRunner])
class EratosthenesWorkerTest extends TestKit(ActorSystem()) with FlatSpecLike with Matchers
{
  implicit val timeout = Timeout(3, TimeUnit.SECONDS)

  val eratosthenesActor = TestActorRef[EratosthenesWorker]

  it should "calculate primes between two numbers" in {
    val future = eratosthenesActor ? CalculatePrimesMessage(2, 2)
    val Success(result: PrimesResult) = future.value.get
    result should be(PrimesResult(List(2)))

    val startOfNaturalNums = eratosthenesActor ? CalculatePrimesMessage(2, 10)
    val Success(naturalResult: PrimesResult) = startOfNaturalNums.value.get
    naturalResult should be(PrimesResult(List(2, 3, 5, 7)))

    val middleOfNumberList = eratosthenesActor ? CalculatePrimesMessage(20, 40)
    val Success(middleListResult: PrimesResult) = middleOfNumberList.value.get
    middleListResult should be(PrimesResult(List(23, 29, 31, 37)))

    val noPrimes = eratosthenesActor ? CalculatePrimesMessage(74, 78)
    val Success(noPrimesResult: PrimesResult) = noPrimes.value.get
    noPrimesResult should be(PrimesResult(List()))

    val oddEndNumber = eratosthenesActor ? CalculatePrimesMessage(2, 25)
    val Success(oddEndNumberResult: PrimesResult) = oddEndNumber.value.get
    oddEndNumberResult should be(PrimesResult(List(2, 3, 5, 7, 11, 13, 17, 19, 23)))

    val regression = eratosthenesActor ? CalculatePrimesMessage(50, 75)
    val Success(regressionResult: PrimesResult) = regression.value.get
    regressionResult should be(PrimesResult(List(53, 59, 61, 67, 71, 73)))
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
