package basics

import scala.annotation.tailrec
import basics.ScalaFunctionsAndMethods.IntTypeExtension

object ScalaFunctionsAndMethods extends App {
  println("------------------------Functions---------------------------")
  println("------------------------Anonymous Function---------------------------")
  /*An anonymous function is one without a name or explicit handle. This can be useful when we need to provide a piece of code or
  action as a parameter to another function*/
  (number: Int) => number + 1
  () => scala.util.Random.nextInt()
  (x: Int, y: Int) => (x + 1, y + 1)
  (number: Int) => {
    println("we are in a anonymous function")
    number + 1
  }

  println("------------------------Named Function---------------------------")
  val inc = (number: Int) => number + 1
  println("Named function which holds Function object - " + inc) //since inc variable only holds the object of Function
  println("Named function - " + inc(4))
  println("Named function with apply method - " + inc.apply(5))

  println("------------------------Closure---------------------------")

  def plot(f: Double => Double): List[(Double, Double)] = {
    val xs: Range = -10 to 10
    xs.map(x => (x.toDouble, f(x))).toList
  }

  val lines: (Double, Double, Double) => Double = (a, b, x) => a * x + b

  val line: (Double, Double) => Double => Double = (a, b) => x => lines(a, b, x)

  val a45DegreeLine = line(1, 0)

  println(plot(a45DegreeLine))
  //Have to see further on closure for better understanding

  println("------------------------Methods---------------------------")
  /*Methods are essentially functions that are parts of a class structure, can be overridden, and use different syntax.
  scala does not allow us to define in anonymous function*/

  def incMethod(number: Int): Int = number + 1

  /*Method definition allow us to skip parenthesis completely for methods without parameters,
  Lets compare a method and function with the same implementation*/

  def randomIntMethod(): Int = scala.util.Random.nextInt()

  val randomIntFunction = () => scala.util.Random.nextInt()

  println("Calling method without parenthesis - " + randomIntMethod())
  println("Calling function without parenthesis - " + randomIntFunction) //it will print object, instead of random number. because if we print variable its just holding the Function object
  println("Calling function with parenthesis - " + randomIntFunction())
  println("Calling function with apply method - " + randomIntFunction.apply()) // same as randomIntFunction()

  //To convert method to function, we can use underscore(_)
  val incFunction = incMethod _
  println("Converted method to function using _ " + incFunction(5))

  println("------------------------Nested Methods---------------------------")
  /*We can use the nesting feature in a situation when some method is tightly coupled with the context of another method.
    The code looks more organized when we use nesting.*/

  def factorial(number: Int): Int = {
    @tailrec
    def nestedFactorial(number: Int, accumulator: Int): Int = {
      if (number == 0) {
        accumulator
      } else {
        nestedFactorial(number - 1, number * accumulator)
      }
    }

    nestedFactorial(number, 1)
  }

  println("Nested method factorial - " + factorial(5))

  println("------------------------Parameterization---------------------------")

  /*In scala, we can parameterize a method by ype. Parameterization allow us to create a genric method with the
  reusable code.*/
  def parameterizationPopMethod[T](listArg: List[T]): T = {
    listArg.head
  } // The type parameter was provided in square brackets during the method declaration

  println("Parameterization Pop Method for type of INT - " + parameterizationPopMethod(List(1, 2, 3, 4, 5)))
  println("Parameterization Pop Method for type of STRING - " + parameterizationPopMethod(List("a", "b", "c", "d", "f")))

  println("------------------------Extension Method---------------------------")
  /*Scala Implicits feautre allow us to provide additional functionality to any existing type.
  We just need to define a new type with methods and to provide a implicit conversion from the new type into an existing type.*/

  //We extended it from AnyVal type, and unnecessary object allocation will be avoided
  //To define an implicit conversion, we have to mark a class as implicit. This allow all methods inside a class to become implicitly accessible
  implicit class IntTypeExtension(val value: Int) extends AnyVal { // Here we wrapping around type of Int
    def isOdd: Boolean = value % 2 != 0
  }

  //Int value 10 doesn't have an explicit method isOdd, but IntTypeExtension class does. The compiler will search for an implicit
  //conversion from Int to IntTypeExtension.
  println("Extension method for type INT - " + 10.isOdd)

  implicit class StringTypeExtension(val value: String) extends AnyVal {
    def capitalizeAlternateWord: String = {
      var count = 0
      val upperOrLowerFunction: (Char => Char) => Char => Char = (fp: Char => Char) => cha => {
        count = count.+(1)
        fp(cha)
      }
      value.map(x => if (count.isOdd) {
        upperOrLowerFunction(x => x.toUpper)(x)
      } else {
        upperOrLowerFunction(x => x.toLower)(x)
      })
    }
  }

  println("Extension method for type STRING - " + "santhosh".capitalizeAlternateWord)
}

class Sample {
  def sample(): Boolean = {
    10.isOdd
  }
}


