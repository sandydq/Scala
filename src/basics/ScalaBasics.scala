package basics

import java.io._
import java.io.File._
import java.util
import java.util.{HashMap, Map}
import scala.annotation.tailrec //Only these two classes

//If we extends the App, everything we write inside will be consider inside a main method
object ScalaBasics extends App {
  println("------------------------Variables & Initialization---------------------------")
  val immutableMsg = "Hi Sandy" //Immutable
  println(immutableMsg)
  var mutableMsg = "How are you" //Mutable
  println(mutableMsg)

  //default value can be initialized using "_", its only applicable to var. we cant use in val because val is a final so
  //we need to instantiate value when we declaring the variable.
  var intValue: Int = _
  var stringValue: String = _
  println(s"Default value: IntValue - $intValue, StringValue - $stringValue")

  //The compiler does not immediately evaluate the bound expression of a lazy val. It evaluates the variable only on its first access.
  lazy val lazyMsg = {
    println("Initializing lazy value ");
    "done"
  } //Lazy initialization only with immutable
  println(lazyMsg)

  println("------------------------Rich Operations---------------------------")
  //Scala provides "rich wrapper" around basic type via implicit conversions
  val max = 0 max 5
  println(s"max - $max")
  val min = 0 min 5
  println(s"min - $min")
  val abs = -2.7.abs
  println(s"abs - $abs")
  val round = -2.7.round
  println(s"round - $round")
  val isInfinity = 1.5.isInfinity
  println(s"isInfinity - $isInfinity")
  val isInfinity2 = (1.0 / 0).isInfinity
  println(s"isInfinity2 - $isInfinity2")
  val range = 4.to(6)
  println(s"range - $range")
  val range2 = 4 to 6
  println(s"range2 - $range2") //same as above
  val capitalize = "sandy".capitalize
  println(s"capitalize - $capitalize")
  val drop = "sandy" drop 2
  println(s"drop - $drop")

  println("------------------------Literals---------------------------")
  //Integer
  val decimal = 31
  val hexaDecimal = 0XFF
  val long = 31L
  val littleShort: Short = 367
  val littleByte: Byte = 38
  println(s"Decimal - $decimal, HexaDecimal - $hexaDecimal, Long - $long, LittleShort - $littleShort, LittleByte - $littleByte")
  //Float
  val double = 1.2345
  val e = 1.234e4 // Double (e or E)
  val float = 1.234F // Float (f or F)
  println(s"double - $double, e - $e, float - $float")
  //Character and String
  val aChar = "D"
  val unicode = '\u0043'
  val string = "S'andy"
  val rawString = """it's "sandy""""
  println(s"aChar - $aChar, unicode - $unicode, string - $string, rawString - $rawString")

  println("------------------------Special Character---------------------------")
  val lineFeed = "Hi\nsandy" // We can also use the unicode value, ex - \u000A
  println(s"LineFeed - $lineFeed")
  val backSpace = "hi\bsandy" //\u0008
  println(s"backSpace - $backSpace") //Deletes the previous character
  val tab = "Hi\tsandy" //\u0009
  println(s"tab - $tab")
  val formFeed = "Hi\fsandy" //\u000C
  println(s"formFeed - $formFeed")
  val carriageReturn = "Hi\rsandy" //\u000D
  println(s"carriageReturn - $carriageReturn") //Deletes the all characters before the specified index of special character
  val doubleQuote = "Hi S\"andy" //\u0022
  println(s"doubleQuote - $doubleQuote")
  val singleQuote = "Hi S\'andy" //\u0027
  println(s"singleQuote - $singleQuote")
  val backslash = "Hi\\Sandy" //\u005c
  println(s"backslash - $backslash")

  println("------------------------Boolean---------------------------")
  val boolean = true
  println(s"Boolean - $boolean")

  println("------------------------Check, Cast and classOf ---------------------------")
  val isInstanceOfVal = "String".isInstanceOf[String]
  println(s"Check - $isInstanceOfVal")
  val asInstanceOfVal = 3.asInstanceOf[Double]
  println(s"Cast - $asInstanceOfVal")
  val classOfVal = classOf[String]
  println(s"classOf  $classOfVal")

  println("------------------------Narrow & Rename Import---------------------------")

  def narrowImport() = {
    import java.math.BigDecimal.ONE //narrow import
    val narrowImport = ONE
    println(s"narrowImport - $narrowImport")
  }

  narrowImport()

  def renameImport(): Unit = {
    import java.math.BigDecimal.{
      TEN => ten,
      ONE => one
    }
    val renameImport = ten
    println(s"rename import - $renameImport")
  }

  renameImport

  println("------------------------Tuples---------------------------")
  val tuples = (99, "Sandy", "1998", 45f) //are immutable and can contains different types of elements
  println(s"tupes_1 - ${tuples._1}")
  println(s"tupes_2 - ${tuples._2}")
  //println(tuples(0)) --> Compile error, its not like list. so we can't call like this.

  println("------------------------Curried functions---------------------------")

  /*Currying is the process of converting a function with multiple arguments into a sequence of functions that take
  one argument. Each function returns another function that consumes the following argument.*/
  def find[A](xs: List[A])(predicate: A => Boolean): Option[A] = {
    xs match {
      case Nil => None
      case head :: tail => if (predicate(head)) Option(head) else find(tail)(predicate)
    }
  }

  val sample = find(List(1, 2, 3, 4, 5))(x => x % 2 == 0)

  val sumVariable: (Int, Int, Int) => Int = (x, y, z) => x + y + z
  val curriedSum: Int => Int => Int = x => y => x + y
  val curriedSumWithCurriedMethod: Int => Int => Int => Int = sumVariable.curried
  println("Curried function Basic Example - " + curriedSum(5)(5))
  println("Curried function Basic Example with curried method - " + curriedSumWithCurriedMethod(10)(10)(5))

  def calculator(operation: Double => Double)(n: Double) = operation(n)

  val multiplyBy5 = calculator(x => x * 5)(5)
  println(s"curried function : multiplyBy5 - $multiplyBy5")
  val safeMinusBy5 = calculator(x => if (x > 5) x - 5 else x)(9)
  println(s"curried function : safeMinusBy5 - $safeMinusBy5")

  def multipleOf(operation: (Double, Double) => Double)(n1: Double, n2: Double) = operation(n1, n2)

  val multipleOf4And5 = multipleOf((x, y) => x * y)(4, 5)
  println(s"curried function : multipleOf4&5 - $multipleOf4And5")

  val multipleOf8And8 = multipleOf((x, y) => x * y)(8, 8)
  println(s"curried function : multipleOf8&8 - $multipleOf8And8")

  println("------------------------Function Literals---------------------------")
  println((1 to 10).toList.filter(_ % 2 == 0))

  println("------------------------Existential Types---------------------------")
  //Labelling something that is unknown
  val marshaller = new Marshaller[String]
  marshaller.marshaller("sandy")
  //  marshaller.marshaller(45d) --> expected: String, actual: Double
  println("correct type - " + marshaller.isInstanceOf[Marshaller[_]])
  println("Wrong type - " + marshaller.isInstanceOf[String])

  println("------------------------Partially Applied Functions---------------------------")

  def sum(a: Int, b: Int, c: Int) = a + b + c

  val sumOfThreeNumbers = sum(5, _: Int, 5)
  println(s"sumOfThreeNumbers -" + sumOfThreeNumbers(5))

  def transform(firstName: String, lastName: String) = firstName.capitalize + " " + lastName.capitalize

  val stringTransform = transform("santhosh", _: String)
  println(s"stringTransform - " + stringTransform("parithi"))

  println("------------------------Call by value, call by name and variable arguments---------------------------")

  var callByValueVariable = 10

  def callByValue(x: Int): Unit = { //Its like a normal function
    println("Call by value 1 - " + x)
    callByValueVariable = x
    println("Call by value 2 - " + x)
  }

  callByValue(5 + callByValueVariable)

  def callByValueRandom(x: Int) = {
    println(s"call by value random 1 - $x and call by value random 2 - $x")
  }

  callByValueRandom(scala.util.Random.nextInt())

  var callByNameVariable = 10

  def callByName(x: => Int): Unit = {
    println("Call by name 1 - " + x)
    callByNameVariable = x;
    println("Call by name 2 - " + x)
  }

  callByName(5 + callByNameVariable) // evaluates the function first, and then evaluates the arguments if need be(if function body doesn't use the respective variable ex., x)

  def callByNameRandom(x: => Int) = {
    println(s"call by name random 1 - $x and call by name random 2 - $x")
  }

  callByNameRandom(scala.util.Random.nextInt())

  def variableArguments(va: Int*): Unit = {
    println("Size of parameters " + va.size)
  }

  variableArguments(1, 2)

  println("------------------------Higher order function---------------------------")
  /*Simply put, we can say that a function is higher-order if it meets one or both of the following conditions:

    it takes one or more functions as parameters
    it returns a function
    */

  def higherOrderFunctionTakingFunctionAsArgument(f: (Int, Int) => Int)(a1: Int): Int = {
    f(a1, a1 + 1)
  }

  val hof1 = higherOrderFunctionTakingFunctionAsArgument((x, y) => x + y)(5)
  println(s"Higher Order Function Taking Function As Argument - " + hof1)

  def higherOrderFunctionReturningFunction(ops: String): (String, String) => String = (x: String, y: String) =>
    ops match {
      case "-" => x + "-" + y
      case "," => x + "," + y
      case _ => x + " " + y
    }

  val hof2 = higherOrderFunctionReturningFunction("Sandy") // Try with "-", ","
  println("Higher Order Function Returning Function - " + hof2("Santhosh", "Parithi"))

  def hofTakingParameterAsArgumentAndReturn(ops1: String => String, ops2: List[String] => Int): (String, String, String) => (String, Int) = { (firstName: String, middleName: String, lastName: String) =>
    (ops1(firstName + " " + middleName + " " + lastName), ops2(List(firstName, middleName, lastName)))
  }

  val notNullFunction: List[String] => Int = _.count(_ != null)
  val hof3 = hofTakingParameterAsArgumentAndReturn(x => x.split(" ").map(x => if (!x.equalsIgnoreCase("null")) x.capitalize else "").mkString(" "), notNullFunction)
  val hof4 = hofTakingParameterAsArgumentAndReturn(x => x.split(" ").map(x => x.toUpperCase).mkString(" "), notNullFunction)
  println("Higher Order Function Taking Function and Returning Function - " + hof3("santhosh", null, "parithi"))
  println("Higher Order Function Taking Function and Returning Function 2  - " + hof4("santhosh", "parithi", "arcot"))


}

class Marshaller[T] {
  def marshaller(t: T): Unit = {
    println(t)
  }
}
