Akka-Sieve-of-Eratosthenes
==========================

Sieve of Eratosthenes in Scala written with TDD

<code>PrimeCalculatorApp</code> is the main entry point to run the application.

To change the number of workers or the end number to go to when calculating primes, modify the following line:

<code>val master = system.actorOf(Props(new EratosthenesMaster(factory, WORKERS, LAST_NUMBER, listener)), name = "master")</code>

where <code>WORKERS</code> is the number of akka actors to create and <code>LAST_NUMBER</code> is the last number to examine when looking for prime numbers.
