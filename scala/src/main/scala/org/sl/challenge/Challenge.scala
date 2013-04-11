package org.sl.challenge

import io.Source
import java.io.File

/**
 *
 * @author Stefan Lourens <stefan.lourens@gmail.com>
 */
object Challenge {

  def main(args: Array[String]) {

    if (!args.isEmpty) {
      val filePath = args.head
      val file = new File(filePath)

      if (file.exists()) {
        val lines = Source.fromFile(filePath).getLines()

        lines foreach {
          line => {
            val parts = line.split(":")

            if (parts.length == 2) {
              val command = parts(0).trim.toUpperCase

              try {
                val numbers = parts(1).split(",") map {
                  n: String => BigDecimal(n.trim)
                }

                if (!numbers.isEmpty) {
                  val result = command match {
                    case "SUM" => numbers.sum
                    case "MIN" => numbers.min
                    case "MAX" => numbers.max
                    case "AVERAGE" => numbers.sum / numbers.size
                    case _ => new AssertionError(s"Invalid command '$command'")
                  }

                  println(s"$command $result")
                }

              } catch {
                case _: NumberFormatException => println("Invalid input number encountered, line ignored")
              }

            } else println("Invalid format, line ignored")
          }
        }

      } else println(s"Could not find file '$filePath'")
    } else println("Usage: run /path/to/file")

  }

}
