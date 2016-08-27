import scala.collection.mutable.{ListBuffer, HashMap}

package object noRedInk {

  def findCommon_one(aarr:Array[Int], barr:Array[Int]):List[Int] = {
    val result = ListBuffer[Int]()
    for (a <- aarr) {
      for (b <- barr) {
        if (a ==b ){
          result.append(a)
        }
      }
    }
    result.toList
  }

  def findCommon_two(aarr:Array[Int], barr:Array[Int]):List[Int] = {
    val result = ListBuffer[Int]()
    val map = HashMap.empty[Int, Int]

    for (i <- Range(0, (aarr.length))) {
      map += (aarr(i)->1)
    }

    for (j <- Range(0, (barr.length))) {
      if (map.contains(barr(j))) {
        result.append(barr(j))
      }
    }
    result.toList
  }

  def main(args: Array[String]): Unit = {

    val aarr = Array(1, 3, 5, 6, 13, 14, 45, 67, 89)
    val barr = Array(2, 4, 7, 13, 15, 17, 23, 24, 26, 29, 45, 69, 78, 89, 92)

    val t0 = System.nanoTime()
    println(findCommon_one(aarr, barr))
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0)/1000 + "us")

    val t2 = System.nanoTime()
    println(findCommon_two(aarr, barr))
    val t3 = System.nanoTime()
    println("Elapsed time: " + (t3 - t2)/1000 + "us")

  }
}
