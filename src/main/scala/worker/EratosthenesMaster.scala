package worker

import akka.actor.{ActorRefFactory, Actor, ActorRef}
import message.{AllPrimesResult, CalculatePrimesMessage, Calculate, PrimesResult}


class EratosthenesMaster(workerRouterFactory:(ActorRefFactory, Int)=> ActorRef, workers: Int, lastNumber: Int, listener: ActorRef) extends Actor {

  val workerRouter = workerRouterFactory(context, workers)

  var primes:List[Int] = List()
  val start: Long = System.currentTimeMillis

  val numsPerChunk = lastNumber / workers
  val chunks = lastNumber / numsPerChunk

  var expectedMessages = chunks

  override def receive: Receive = {
    case Calculate => {

      workerRouter ! CalculatePrimesMessage(2, numsPerChunk)
      for(middleChunk <- 1 until chunks) {
        workerRouter ! CalculatePrimesMessage(numsPerChunk * middleChunk, numsPerChunk * middleChunk + numsPerChunk)
      }
    }
    case PrimesResult(value) => {
      primes = primes ::: value
      expectedMessages = expectedMessages - 1
      if(expectedMessages == 0) {
        listener ! AllPrimesResult(primes, duration = System.currentTimeMillis - start)
        context.stop(self)
      }
    }

  }
}
