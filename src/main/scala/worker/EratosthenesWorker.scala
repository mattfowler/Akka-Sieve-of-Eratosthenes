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
    val numbers = mutable.ArrayBuffer.tabulate(end - start + 1)(i => i + start)
    var oddNumbers = numbers.filter(_ % 2 != 0)

    val intSqrt = Math.sqrt(end).toInt
    for (i <- 3 to end by 2 if i <= intSqrt) {
      var firstNonPrime = ((start + i - 1) / i) * i
      if (firstNonPrime < i * i) {
        firstNonPrime = i * i
      }
      firstNonPrime = if (firstNonPrime % 2 == 0) firstNonPrime + i else firstNonPrime
      for (nonPrime <- firstNonPrime to end by 2 * i) {
        val index: Int = nonPrime - start
        oddNumbers.update(index / 2, 0)
      }
    }
    if (start == 2) {
      oddNumbers =  2 +: oddNumbers
    }
    oddNumbers.filter( _ != 0).toList
  }

}
