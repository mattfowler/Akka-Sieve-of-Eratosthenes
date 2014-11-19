package worker

import akka.actor.Actor
import message.AllPrimesResult

class EratosthenesCompletionListener extends Actor {
  def receive = {
    case AllPrimesResult(primes, duration) =>
      println("took %s milliseconds to calculate %s primes:".format(duration, primes.length))
      context.system.shutdown()
  }

}
