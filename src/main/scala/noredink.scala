import scala.collection.mutable.ListBuffer
import scala.io.{StdIn, Source}
import scala.util.Random

package object noRedInk {

  def readCSV(fileName:String) = {
    Source
      .fromURL(getClass.getClassLoader.getResource(fileName))
      .getLines
      .toList
      .tail //drop header line
  }

  def findQuestions(questions:List[String], numOfQuestions:Int) = {

    val numOfStrands = questions
      .last
      .split(",")
      .toList(0)
      .toInt

    //strandUsage will contain how much time to use each strand
    val strandUsage = distribute(numOfQuestions, numOfStrands)
    val questionIDs = ListBuffer[Int]()
    for (i <- Range.inclusive(1, numOfStrands)) {
      questionIDs.appendAll(getQuestionsFromStrand(questions, strandUsage, i))
    }
    val selectedQs = questionIDs.toList

    print("Selected question IDs (" + numOfQuestions + "): " )
    selectedQs map (n => print(n + " "))
    println("")
  }

  def distribute(n:Int, slots:Int):List[Int] = {

    var result = Array.fill[Int](slots)(0)
    for (i <- Range.inclusive(1, n)) {
      result(i%slots) += 1
    }
    result.toList
  }

  def getQuestionsFromStrand(
    questions:List[String],
    strandUsage:List[Int],
    currentStrand:Int):ListBuffer[Int] = {
   
    def getThisStrand(questions:List[String], n: Int):List[String] = {
      questions filter (_.split(",").toList.head.toInt == n)
    }

    def getThisStandard(questions:List[String], n: Int):List[String] = {
      questions filter (_.split(",").toList(2).toInt == n)
    }

    def selectQuestions(
      questions:List[String],
      n:Int,
      prevQs:List[String]):List[String] = {

      if (n <= questions.length) {
        Random.shuffle(questions).take(n)
      } else {
        val result = ListBuffer[String]()
        val multiplier = n / (questions.length)
        Range.inclusive(1, multiplier).foreach(i => result.appendAll(questions))
        result.toList ++ Random.shuffle(questions).take(n-(multiplier*questions.length))
      }
    }

    val result=ListBuffer[String]()
    val questionsInStrand = getThisStrand(questions, currentStrand)
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

    val standardUsage = distribute(
      strandUsage(currentStrand-1),
      maxStandard - minStandard + 1)

    for (j <- Range.inclusive(minStandard, maxStandard)){
      val availableQuestions = getThisStandard(questionsInStrand, j)
      val neededQuestions =
        selectQuestions(
          availableQuestions,
          standardUsage(j-minStandard),
          List())
      result.appendAll(neededQuestions)
    }
    
    result map (_.split(",").dropRight(1).last.toInt)
  }

  def main(args: Array[String]): Unit = {

    val allQuestions = readCSV("questions.csv")

    //val numOfQuestions = StdIn.readLine("Number of wanted questions:  ").toInt

    findQuestions(allQuestions, 0)
    findQuestions(allQuestions, 2)
    findQuestions(allQuestions, 3)
    findQuestions(allQuestions, 6)
    findQuestions(allQuestions, 7)
    findQuestions(allQuestions, 8)  // first usage of recursion
    findQuestions(allQuestions, 20)
  }
}
