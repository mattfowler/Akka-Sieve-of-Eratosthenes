package worker

import akka.actor.Actor
import exception.InvalidInputException
import message.{CalculatePrimesMessage, PrimesResult}


class EratosthenesWorker extends Actor {

  def receive = {
    case CalculatePrimesMessage(start, end) => sender ! PrimesResult(calculatePrimes(start, end))
  }

  def calculatePrimes(start: Int, end: Int): List[Int] = {
    if(start < 1 || end < 1 || start > end) {
      throw new InvalidInputException("Start and end must be nonzero, positive and start must be less than end")
    }
    List()
  }

}
