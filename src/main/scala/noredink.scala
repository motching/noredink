import scala.io.{StdIn, Source}
import scala.collection.mutable.ListBuffer

package object noRedInk {

  def getThisStrand(questions:List[String], n: Int):List[String] = {
    questions filter (_.split(",").toList.head.toInt == n)
  }

  def getThisStandard(questions:List[String], n: Int):List[String] = {
   questions filter (_.split(",").toList(2).toInt == n)
  }

  def distribute(n:Int, slots:Int):List[Int] = {
    var result = Array.fill[Int](slots)(0)
    for (i <- Range.inclusive(1, n)) {
      result(i%slots) += 1
    }
    result.toList
  }

  def getQuestionsFromStrand(questions:List[String], strandUsage:List[Int], currentStrand:Int):ListBuffer[Int] = {

    val result=ListBuffer[String]()
    val questionsInStrand = getThisStrand(questions, currentStrand+1)
    val minStandard = questionsInStrand
      .head
      .split(",")
      .toList(2)
      .toInt
    val maxStandard = questionsInStrand
        .last
        .split(",")
        .toList(2)
        .toInt
    val standardUsage = distribute(questionsInStrand.length, maxStandard - minStandard)

    for (i <- Range.inclusive(1, strandUsage(currentStrand))){
      val availableQuestions = getThisStandard(questionsInStrand, i)

      val neededQuestions = availableQuestions.take(standardUsage(i-1))

      result.appendAll(neededQuestions)
    }

    ListBuffer[Int]()
  }

  def main(args: Array[String]): Unit = {

    //val numOfQuestions = StdIn.readLine("Number of wanted questions:  ").toInt
    val numOfQuestions = 5

    val questions = Source
      .fromURL(getClass.getClassLoader.getResource("questions.csv"))
      .getLines
      .toList
      .tail //drop header line

    val numOfStrands = questions
      .last
      .split(",")
      .toList(0)
      .toInt

    //this will contain how much time to use each strand
    val strandUsage = distribute(numOfQuestions, numOfStrands)

    val questionIDs = ListBuffer[Int]()

    for (i <- Range.inclusive(1, numOfStrands)) {
      questionIDs.appendAll(getQuestionsFromStrand(questions, strandUsage, i-1))
    }

    println("solution: ")
    questionIDs map println

    val strandOne = getThisStrand(questions, 1)


    val t0 = System.nanoTime()

    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0)/1000 + "us")

  }
}
