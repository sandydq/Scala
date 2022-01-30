package basics

object Currying extends App {
  println("------------------------Curried function---------------------------")

  /*Currying is the process of converting a function with multiple arguments into a sequence of functions that take
  one argument. Each function returns another function that consumes the following argument.*/
  val sumVariable: (Int, Int, Int) => Int = (x, y, z) => x + y + z
  val curriedSum: Int => Int => Int = x => y => x + y
  val curriedSumWithCurriedMethod: Int => Int => Int => Int = sumVariable.curried
  println("Curried function Basic Example - " + curriedSum(5)(5))
  println("Curried function Basic Example with curried method - " + curriedSumWithCurriedMethod(10)(10)(5))

  println("------------------------Curried method---------------------------")

  //We can achieve the same for methods because scala give us the ability to create multiple argument lists
  def sumMethod(x: Int, y: Int): Int = x + y

  //There is even a way to convert a method with multiple arguments to curried functions. -> Method to function(_) and then function to curried function(.curried)
  val curriedFunctionFromNormalMethod: Int => Int => Int = (sumMethod _).curried
  //sum _ converts a method with multiple arguments into a function with multiple arguments (called eta-expansion) on which we can call curried

  //Also eta-expansion will convert multiple arguments lists method into a curried function without a curried call
  def curriedFunctionWithoutCurriedMethod: Int => Int => Int = curriedFunctionFromNormalMethod

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

  println("------------------------Partial application---------------------------")
  /*Partial application is the process of reducing the number of arguments by applying some of them when the method or function
  is created. lets use partial application and curried sum function to create the increment function. */

  //function
  val curriedSum2: Int => Int => Int = x => y => x + y
  val increment: Int => Int = curriedSum2(1)
  val incrementBy2: Int => Int = curriedSum2(2)
  println("Partial application function increment list by 1 - " + (1 to 10).map(increment))
  println("Partial application function increment list by 2 - " + (1 to 10).map(incrementBy2))

  //method
  def curriedMethod2(x: Int)(y: Int): Int = x + y

  val increment2: Int => Int = curriedMethod2(1)
  val increment2By2: Int => Int = curriedMethod2(2)
  println("Partial application method increment list by 1 - " + (1 to 10).map(increment2))
  println("Partial application method increment list by 2 - " + (1 to 10).map(increment2By2))

  println("------------------------Type Inference---------------------------")
  /*Type inference takes into account only one parameter list at the time. That means that in some cases, we can help the
    compiler to derive the proper type.*/

  //As an example, we will create a find method that returns the first element satisfying a given predicate.
  def find[A](xs: List[A], predicate: A => Boolean): Option[A] = {
    xs match {
      case Nil => None
      case head :: tail => if (predicate(head)) Option(head) else find(tail, predicate)
    }
  }

  //  find((1 to 10).toList, x => x % 2 == 0) --> compiler error

  /*the above code wont compile because the compiler cannot figure out what type x is. we can resolve the problem by
    defining the type as Int*/
  println("Type inference - " + find((1 to 10).toList, (x: Int) => x % 2 == 0))

  /*This works, but we can help the compiler with type inference by moving the predicate function into the second
    argument list.*/

  def findTypeInference[A](xs: List[A])(predicate: A => Boolean): Option[A] = {
    xs match {
      case Nil => None
      case head :: tail => if (predicate(head)) Option(head) else findTypeInference(tail)(predicate)
    }
  }

  val sample = findTypeInference(List(1, 2, 3, 4, 5))(x => x % 2 == 0)

  println("------------------------Flexibility---------------------------")
  /*Currying and partial application make it possible to create smaller functions of differing behaviour by applying some
  arguments to the curried function. Lets make our own sum function more genric by adding mapping function f: Int => Int, also
  known as the identity function. which will map both values before adding them together.*/

  def sumF(f: Int => Int)(x: Int, y: Int): Int = f(x) + f(y)

  val flexibilitySum: (Int, Int) => Int = sumF(identity) //A method that returns its input value.
  println("Flexibility of sum - " + flexibilitySum(2, 3))

  val flexibilitySumSquare: (Int, Int) => Int = sumF(x => x * x)
  println("Flexibility of square sum - " + flexibilitySumSquare(5, 5))

  val flexibilityIncrement = flexibilitySum.curried(1)
  val flexibilityDecrement = flexibilitySum.curried(-1)

  println("Flexibility Increment - " + flexibilityIncrement(5))
  println("Flexibility Decrement - " + flexibilityDecrement(5))

  //Curried function are useful when one argument function is expected

  //For example, lets consider a situation in which we have a list of numbers, and we want to increment each of them using sum function:
  val sum: (Int, Int) => Int = (x, y) => x + y
  println("Flexibility Increment List - " + (1 to 5).map(n => sum(1, n)))
  /*For above code to work we need to explicitly pass n into the sum function as the second argument. To avoid this and improve our code
  readability, we can use currying*/
  val flexibilityCurriedSum = sum.curried // same as val flexibilityCurriedSum: Int => Int => Int = x => y => x + y
  println("Flexibility Increment List by curried- " + (1 to 5).map(flexibilityCurriedSum(1)))

  println("------------------------Sample of function and method calling---------------------------")
  val sand = (x: Int) => (y: Int) => x + y
  val sand2: (Double => Double) => Double => Double = x => y => x(y)
  val sand3: Double => (Double => Double) => Double = x => y => y(x)

  val res = sand(2)
  val res2 = sand2(x => x)
  val res3 = sand3(5)

  def mend(x: Int)(y: Int): Int = x + y

  def mend2(f1: Double => Double)(x: Double): Double = f1(x)

  def mend3(x: Double)(f1: Double => Double): Double = f1(x)

  //  val mes = mend(5) --> Compile error
  val mesCorrect: Int => Int = mend(5)
  //  val mes2 = mend2(x => x) --> Compile error
  val mes2Correct: Double => Double = mend2(x => x)
  //  val mes3 = mend3(5) --> Compile error
  val mes3Correct: (Double => Double) => Double = mend3(5)
}
