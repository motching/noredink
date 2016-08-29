import scala.io.{StdIn, Source}

package object noRedInk {

  def main(args: Array[String]): Unit = {

    //val numOfQuestions:Int = StdIn.readLine("Number of wanted questions:  ").toInt
    //println("nums: " + numOfQuestions.toString)

    val questions = Source
      .fromURL(getClass.getClassLoader.getResource("questions.csv"))
      .getLines
      .toList
      .tail

    val usage = Source
      .fromURL(getClass.getClassLoader.getResource("usage.csv"))
      .getLines
      .toList
      .tail

    val lastRow = questions
      .last
      .split(",")
      .toList

    val numOfStrands = lastRow(0).toInt
    val numOfStandards = lastRow(2).toInt

    println("strands " + numOfStrands)
    println("standards " + numOfStandards)

    val t0 = System.nanoTime()

    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0)/1000 + "us")

  }
}
