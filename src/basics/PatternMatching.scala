package basics

import scala.annotation.tailrec

object PatternMatching extends App {
  val bools = Seq(true, false)
  for (bool <- bools) {
    bool match {
      case true => println("Got heads")
      case false => println("Got tails")
    }
  }

  //Alternative way
  for (bool <- bools) {
    val which = if (bool) "head" else "tails"
    println("Got " + which)
  }

  println("------------------Values, Variables and Types in Matches-------------------")
  //Values, Variables and Types in Matches
  for (x <- Seq(1, 2, 2.7, "one", "two", 'four)) {
    val str = x match {
      case 1 => "int 1"
      case i: Int => "other int: " + i
      case d: Double => "a double: " + x
      case "one" => "string one"
      case s: String => "other string: " + s
      case unexpected => "unexpected value: " + unexpected
    }
    println(str)
  }
  //We replaced i,d,s and unexpected with the placeholder _.
  for (x <- Seq(1, 2, 2.7, "one", "two", 'four)) {
    val str = x match {
      case 1 => "int 1"
      case _: Int => "other int: " + x
      case _: Double => "a double: " + x
      case "one" => "string one"
      case _: String => "other string: " + x
      case _ => "unexpected value: " + x
    }
    println(str)
  }


  //There are few rules we have to keep in mind when we writing case clauses.
  //compiler assumes term starts with capital letter is a type name.
  //while term begins with a lowercase letter is assumed as a name of the variable that will hold
  //an extracted or matched value
  def checkY(y: Int) = {
    for {
      x <- Seq(99, 100, 101)
    } {
      println(x match {
        case y => "found y!"
//        case Y => "found y!" -> upper letter will be compiled as type, so Y is not a type
        case i: Int => "int: "+ i
      })
    }
  }
  checkY(100)

  def checkY2(y: Int) = {
    for {
      x <- Seq(99, 100, 101)
    } {
      println(x match {
        case `y` => "found y!" //"back ticks" -> to indicate we really want to match against the value held by y
        case i: Int => "int: "+ i
      })
    }
  }
  checkY2(100)

  //OR |
  for {
    x <- Seq(1,2,2.7,"one","two",'four)
  } {
    println(x match {
      case _: Int | _: Double => "a number: " + x
      case unexpected => "unexpected value: "+ unexpected
    })
  }

  //Matching on Sequences
  println("------------------Matching on Sequences-------------------")
  val nonEmptySeq = Seq(1,2,3,4,5)
  val emptySeq = Seq.empty[Int]
  val nonEmptyList = List(1,2,3,4,5)
  val emptyList = Nil //List.empty[Int]
  val nonEmptyVector = Vector(1,2,3,4,5)
  val emptyVector = Vector.empty[Int]
  val nonEmptyMap = Map("one" -> 1, "two" -> 2, "three" -> 3)
  val emptyMap = Map.empty[String,Int]

  def seqToString[T](seq: Seq[T]): String = seq match {
    case head +: tail => s"$head +:" + seqToString(tail)
    case Nil => "Nil"
  }

  for (seq <- Seq(nonEmptySeq, emptySeq, nonEmptyList, emptyList, nonEmptyVector, emptyVector, nonEmptyMap.toSeq, emptyMap.toSeq)){
    println(seqToString(seq))
  }

  println("-------------------------------")
  //Seq conceptually like a linked list, where each head node holds an element and it points to the tail(the rest of the sequence)
  //(node1, (node2, (node3, (node4, (end)))))
  //Nil -> Object used for lists to match all empty sequences. we can use Nil even for collections that aren't a List
  def seqToString2[T](seq: Seq[T]): String = seq match {
    case head +: tail =>
//        s"(${seqToString2(tail)} +: $head)" // it will print list in reverse order
      s"($head +: ${seqToString2(tail)})"
    case Nil => "(Nil)"
  }

  for (seq <- Seq(nonEmptySeq, emptySeq, nonEmptyMap.toSeq)){
    println(seqToString2(seq))
  }

  //Methods that end with a colon (:) bind to the right, towards the Seq tail
  val s1 = (1 +: (2 +: (3 +: (4 +: Nil))))
  val l1 = (1 :: (2 :: (3 :: (4 :: Nil))))
  val s2 = (("one",1) +: ("two",2) +: ("three",3) +: Nil)

  //Methods that starts with a colon (:) bind to the left, towards the Seq tail
  val s3 = Nil :+ 1 :+ 2 :+ 3 :+ 4
  //Map.apply factory method expects a variable arguments list of two element tuples.
  //So in order to use the sequence s2 to construct a Map, we had to use the (:_*) idiom for the compiler to convert it to a variable-argument list
  val m1 = Map(s2 :_*)

  println("------------------Matching on Tuples-------------------")
  //Matching on Tuples
  val langs = Seq(("scala", "martin", "odersky"),("clojure", "rich", "hickey"),("lisp", "John", "McCarthy"))

  for (lang <- langs) {
    lang match {
      case ("scala", _, _) => println("Found Scala")
      case (lang, first, last) => println(s"Found other language: $lang ($first, $last)")
    }
  }

  println("------------------Guards in case Clauses-------------------")
  //Sometimes you need a little additional logic.
  for (i <- Seq(1,2,3,4)) {
    i match {
      case _ if i%2 == 0 => println(s"even: $i")
      case _ => println(s"odd: $i")
    }
  }

  println("------------------Matching on case Classes-------------------")
  case class Address(street: String, city: String, country: String)
  case class Person(name: String, age: Int, address: Address)

  val alice  = Person("Alice", 25, Address("1 Scala lane", "Chicago", "USA"))
  val bob  = Person("Bob", 29, Address("2 Java Ave.", "Miami", "USA"))
  val charlie  = Person("Charlie", 32, Address("3 Python Ct.", "Boston", "USA"))

  for (person <- alice +: bob +: charlie +: Nil) {
    person match { //whenever we pattern match the case class, it will call unapply(deconstruct method) to convert Object into tuple
      case Person("Alice", 25, Address(_, "Chicago", _)) => println("Hi Alice!")
      case Person("Bob", 29, Address("2 Java Ave.", "Miami", "USA")) => println("Hi Bob!")
      case Person(name, age, _) => println(s"Who are you, $age year-old person named $name?")
    }
  }

  println("-------------------------------------------zipWithIndex------------------------------------------")
  //Seq.zipWithIndex
  val itemCosts = ("Pencil", 0.52) :: ("Paper", 1.35) :: ("Notebook", 2.43) :: Nil
  val itemCostsWithIndices = itemCosts.zipWithIndex
  for (itemCostIndex <- itemCostsWithIndices) {
    itemCostIndex match {
      case ((item,cost), index) => println(s"$index: $item costs $cost each")
    }
  }

  @tailrec
  def processSeq2[T](l: Seq[T]): Unit = l match {
    case +:(head,tail) =>
      printf("%s +:", head)
      processSeq2(tail)
    case Nil => print("Nil")
  }
  processSeq2(List(1,2,3,4,5))
  println()
  //Types with two types of parameters can be written with infix notation and so can case clauses.
  case class With[A,B](a:A, b:B)
  val with1: With[String,Int] = With("Foo", 1)
  val with2: String With Int = With("Bar", 1)

  Seq(with1,with2) foreach {
    case s With i => println(s"$s with $i")
    case w => println(s"unknown: $w")
  }

  def reverseSeqToString[T](l: Seq[T]): String = l match {
    case prefix :+ end => reverseSeqToString(prefix) + s" :+ $end" // case :+(prefix, end)
    case Nil => "Nil"
  }
  for (seq <- Seq(nonEmptyList, nonEmptyVector, emptyList, Seq(1), nonEmptyMap.toSeq)) {
    println(reverseSeqToString(seq))
  }

  println("------------------------------------Windows Sliding--------------------------------------")
  //unapplySeq Method -> what if we want more flexibility to return a sequence of extracted items, rather than a foxed number of them?
  def windows[T](seq: Seq[T]): String = seq match {
    case Seq(head1, head2, _*) => s"($head1, $head2), " + windows(seq.tail) //case Seq(head1, head2, vals @_*) -> vals will hold the extracted values
    case Seq(head, _*) => s"($head, _), " + windows(seq.tail)
    case Nil => "Nil"
  }
  for (seq <- Seq(nonEmptyList, emptyList, nonEmptyMap.toSeq)) {
    println(windows(seq))
  }
  //We could still use the +: matching we saw before, which is more elegant.
  def windows2[T](seq: Seq[T]): String = seq match {
    case head1 +: head2 +: tail => s"($head1, $head2), " + windows(seq.tail)
    case head +: tail => s"($head, _), " + windows(seq.tail)
    case Nil => "Nil"
  }
  for (seq <- Seq(nonEmptyList, emptyList, nonEmptyMap.toSeq)) {
    println(windows(seq))
  }

  println("------------------------------------Sliding--------------------------------------")
  //Sliding
  val seq = Seq(1,2,3,4,5)
  val slide2 = seq.sliding(2)
  val slide3 = seq.sliding(3)
  println(slide2.toList)
  println(slide2.toSeq)
  println(slide3.toList)
  println(seq.sliding(3,2).toList)

  println("------------------------------------Matching on Variable Argument Lists--------------------------------------")

  object Op extends Enumeration {
    type Op = Value

    val EQ = Value("=")
    val NE = Value("!=")
    val LTGT = Value("<>")
    val LT = Value("<")
    val LE = Value("<=")
    val GT = Value(">")
    val GE = Value(">=")
  }
  import Op._
  case class WhereOp[T](columnName: String, op: Op, value: T)
  case class WhereIn[T](columnName: String, op: T, vals: T*)

  val wheres = Seq(WhereIn[String]("state", "IL", "CA", "VA"),
    WhereOp("state", EQ, "IL"), WhereOp("name", EQ, "Buck Trends"), WhereOp[Int]("age", GT, 29))

  for (where <- wheres) {
    where match {
      case WhereIn(col, val1, vals @_*) =>
        val valstr = (val1 +: vals).mkString(", ")
        println(s"WHERE $col In ($valstr)")
      case WhereOp(col, op, value) => println(s"WHERE $col In $value")
      case _ => println(s"ERROR: Unknown expression: $where")
    }
  }
}

