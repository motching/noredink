package object noRedInk {

  def main(args: Array[String]): Unit = {

    val t0 = System.nanoTime()
    println("I work!")
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0)/1000 + "us")

  }
}
