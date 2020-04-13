import scala.collection.Searching
import scala.math
import scala.util.Sorting
import scala.collection.Searching._

/**
 * Точка считается принадлежащей отрезку, если она находится внутри него или на границе.
 * Для каждой точки в порядке появления во вводе выведите, скольким отрезкам она принадлежит.
 *
 * точка НЕ лежит на данном отрезке только в двух случаях:
 * 1) Если координата конца отрезка меньше координаты точки;
 * 2) Если координата начала отрезка больше координаты точки;
 * В любом другом случае точка лежит на данном отрезке
 */
object Main {
  def bsPredicate(num: Int, pred: Int => Boolean, lastOccurence: Boolean = false, prevIndex: Option[Int] = None, array: Array[Int],l: Int, r: Int): Int = {
    println(s"check: l=$l, r=$r,num=$num, array=${array.mkString(" ")}")
    val index = (l + r) / 2
    println(s"index $index")
    if (l > r) {
      prevIndex.map(_ + 1).getOrElse(0)
    } else {
      val am = array(index)
      println(s"am=$am")
      if (am <= num) {
        if (pred(am)) {
          if (lastOccurence) {
            bsPredicate(num, pred, lastOccurence, Option(index), array, index + 1, r)
          } else {
            index + 1
          }
        } else {
          bsPredicate(num, pred, lastOccurence, prevIndex, array, index + 1, r)
        }
      } else {
        bsPredicate(num, pred, lastOccurence, prevIndex, array, l, index - 1)
      }
    }
  }
  def solve(segmentCount: Int, pointCount: Int, segments: Array[(Int,Int)], points: Array[Int]): Unit = {
    val sortedLeft = segments.clone().map(_._1)
    Sorting.quickSort(sortedLeft)
    val sortedRight = segments.clone().map(_._2)
    Sorting.quickSort(sortedRight)

    println(sortedLeft.mkString(","))
    println(sortedRight.mkString(","))

    def leftCount(point: Int) =
      bsPredicate(point, _ <= point, true, None, sortedLeft,0,sortedLeft.length - 1)
    def rightCount(point: Int) =
      bsPredicate(point, _ < point, true, None, sortedRight,0,sortedRight.length - 1)

    def resultCount(point: Int) = {
      val l = leftCount(point)
      val r = rightCount(point)
      println(s"point = $point, leftCount = $l")
      println(s"point = $point, rightCount = $r")
      l - r
    }

    println(points.map(resultCount).mkString(" "))
  }
  def main(args: Array[String]): Unit = {
    val (segmentCount,pointCount)  = {
      val s = scala.io.StdIn.readLine().split(" ")
      s.head.toInt -> s.last.toInt
    }
    val segments = (1 to segmentCount).map(_ => {
      val s = scala.io.StdIn.readLine().split(" ")
      s.head.toInt -> s.last.toInt
    }).toArray
    val points = scala.io.StdIn.readLine().split(" ").map(_.toInt)

    solve(segmentCount, pointCount, segments, points)
  }
  def test(): Unit = {
    // 1 0 0
    solve(2,3,Array((0,5),(7,10)),Array(1,6,11))

    // 1 2
    solve(2,2,Array((1,2),(2,2)),Array(1,2))

    /**
     * 6 6
     * 0 3
     * 1 3
     * 2 3
     * 3 4
     * 3 5
     * 3 6
     *
     * 1 2 3 4 5 6
     *
     * Ответ 2 3 6 3 2 1
     */
    solve(6,6,Array((0,3),(1,3),(2,3),(3,4),(3,5),(3,6)),Array(1,2,3,4,5,6))
  }
}
Main.test()
