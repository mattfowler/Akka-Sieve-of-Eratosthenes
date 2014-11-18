package worker

import akka.actor.Actor
import message.{CalculatePrimesMessage, PrimesResult}

class EratosthenesWorker extends Actor {

  def receive = {
    case CalculatePrimesMessage => sender ! PrimesResult(List())
  }

  def calculatePrimes(start: Integer, end: Integer): List[Integer] = {
    List()
  }

}
