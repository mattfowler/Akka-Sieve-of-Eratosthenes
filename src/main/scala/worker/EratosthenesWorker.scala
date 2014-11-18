package worker

import akka.actor.Actor
import exception.InvalidInputException
import message.{CalculatePrimesMessage, PrimesResult}

import scala.collection.mutable

class EratosthenesWorker extends Actor {

  def receive = {
    case CalculatePrimesMessage(start, end) => sender ! PrimesResult(calculatePrimes(start, end))
  }

  def calculatePrimes(start: Int, end: Int): List[Int] = {
    if(start < 1 || end < 1 || start > end) {
      throw new InvalidInputException("Start and end must be nonzero, positive and start must be less than end")
    }
    var numbers = mutable.ArraySeq.tabulate(end - start + 1)(i => i  + start)
    var primes: List[Int] = List()

    while(numbers.length > 0) {
      val nextPrime:Int = numbers.head
      numbers = numbers.drop(1)
      for(i <- nextPrime * nextPrime to end by nextPrime ) {
        numbers = numbers diff mutable.ArraySeq(i)
      }
      primes = primes :+ nextPrime
    }

    primes
  }

}
