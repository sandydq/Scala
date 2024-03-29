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
  def checkY(y: Int): Unit = {
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

  def checkY2(y: Int): Unit = {
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

  println("---------------------------------More on Type Matching---------------------------------------")
  for {
    x <- Seq(List(5.5,5.6,5.7), List("a", "b"))
  } {
    println(x match {
      case seqD: Seq[Double] => ("seq double", seqD)
      case seqS: Seq[String] => ("seq string", seqS)
      case _ => ("unknown!", x)
    })
  } //This code will give warning by a compiler. Because compiler will know given object is a List, it can't check at runtime that its a List[Double] or a List[String]

  //Effective workaround is to match on the collection first, then use a nested match on the head of the element to determine the type
  def doSeqMatch[T](seq: Seq[T]): String = seq match {
    case Nil => "Nothing"
    case head +: _ => head match {
      case _ : Double => "Double"
      case _ : String => "String"
      case _ => "Unmatched seq element"
    }
  }

  for (x <- Seq(List(5.5,5.6,5.7), List("a","b"), Nil)) {
    println(x match {
      case seq : Seq[_] => (s"seq ${doSeqMatch(x)}", seq)
      case _ => ("unknown!", x)
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

  for (person <- alice +: bob +: charlie +: Nil) {
    person match { //whenever we pattern match the case class, it will call unapply(deconstruct method) to convert Object into tuple
      case p @ Person("Alice", 25, address) => println(s"Hi Alice! $p")
      case p @ Person("Bob", 29,a @ Address(street, city, country)) => println(s"Hi ${p.name}! age ${p.age}, in ${a.city}")
      case p @ Person(name, age, _) => println(s"Who are you, $age year-old person named $name? ${p.address.city}")
    }
  }
  //If we are not extracting fields from the Person instance, we can just wrote p: Person => ...

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

  println("---------------------------------Matching on Regular Expressions------------------------------")
  val BookExtractorRE = """Book: title=([^,]+),\s+author=(.+)""".r
  val MagazineExtractorRE = """Magazine: title=([^,]+),\s+issue=(.+)""".r

  val catalog = Seq(
    "Book: title=Programming Scala Second Edition, author=Dean Wampler",
    "Magazine: title=The New Yorker, issue=January 2014",
    "Unknown: text=Who put this here??")

    for (item <- catalog) {
      item match {
        case BookExtractorRE(title, author) => println(s"""Book "$title", written by $author""")
        case MagazineExtractorRE(title, issue) => println(s"""Magazine "$title", issue $issue""")
        case entry => println(s"Unrecognized entry: $entry")
      }
    }

  println("---------------------------------Sealed Hierarchies and Exhaustive Matches------------------------------------")
  sealed abstract class HttpMethod() {
    def body: String
    def bodyLength: Int = body.length
  }

  case class Connect(body: String) extends HttpMethod
  case class Delete(body: String) extends HttpMethod
  case class Get(body: String) extends HttpMethod
  case class Head(body: String) extends HttpMethod
  case class Options(body: String) extends HttpMethod
  case class Post(body: String) extends HttpMethod
  case class Put(body: String) extends HttpMethod
  case class Trace(body: String) extends HttpMethod

  def handle(method: HttpMethod) = method match {
    case Connect(body) => s"Connect: (length: ${method.bodyLength}) $body"
    case Delete(body) => s"Connect: (length: ${method.bodyLength}) $body"
    case Get(body) => s"Connect: (length: ${method.bodyLength}) $body"
    case Head(body) => s"Connect: (length: ${method.bodyLength}) $body"
    case Options(body) => s"Connect: (length: ${method.bodyLength}) $body"
    case Post(body) => s"Connect: (length: ${method.bodyLength}) $body"
    case Put(body) => s"Connect: (length: ${method.bodyLength}) $body"
    case Trace(body) => s"Connect: (length: ${method.bodyLength}) $body"
  }

  val methods = Seq(
    Connect("connect body..."),
    Delete("delete body..."),
    Get("get body..."),
    Head("head body..."),
    Options("options body..."),
    Post("post body..."),
    Put("put body..."),
    Trace("trace body..."))
  methods foreach (method => println(handle(method)))

  println("---------------------------------Other Uses of Pattern Matching------------------------------------")
  //The powerful feature is not limited to case clauses. you can use pattern matching when defining values, including in for comprehensions

  val Person(name, age, Address(_, state, _)) = Person("Dean", 29, Address("1 Scala Way", "CA", "USA"))
  println(s"name - $name, age - $age, state - $state")

  val head +: tail = List(1,2,3,4)
  println(s"head - $head, tail - $tail")

  val head1 +: head2 +: tail2 = List(1,2,3,4)
  println(s"head1 - $head1, head2 - $head2, tail1 - $tail2")

  val Seq(a,b,c) = Seq(1,2,3)
  println(s"a - $a, b - $b, c - $c")

  try {
    val Seq(a1,b2,c2) = Seq(1,2,3,4) //MatchError - Solution - Seq(a1, b1, vals @_*)
  } catch {
    case e: Exception => println(s"Exception - ${e.getLocalizedMessage}")
  }

  val p = Person("Dean", 29, Address("1 Scala Way", "CA", "USA"))
  println(if (p == Person("Dean", 29, Address("1 Scala Way", "CA", "USA"))) "yes" else "no")
  println(if (p == Person("1 Scala Way", 29, Address("1 Scala Way", "CA", "USA"))) "yes" else "no")

  //_ placeholder wont work here
  //  println(if (p == Person(_, 29, Address(_, _, "USA"))) "yes" else "no") -> error

  def sum_count(ints: Seq[Int]) = (ints.sum, ints.size)
  val (sum, count) = sum_count(List(1,2,3,4))
  println(s"sum $sum and size $count")

  val dogBreeds = Seq(Some("Doberman"), None, Some("Yorkshire Terrier"), Some("Dachshund"), None, Some("Scottish Terrier"),
    None, Some("Great Dane"), Some("Portuguese Water Dog"))

  for {
    Some(breed) <- dogBreeds //Only gets Some(values)
    uppercaseBreed = breed.toUpperCase
  } println(uppercaseBreed)

  case class Person2(name: String, age: Int)
  val as = Seq(Address("1 Scala Lane", "Anytown", "USA"), Address("2 Clojure Lane", "Othertown", "USA"))
  val ps = Seq(Person2("Black Trends", 29), Person2("Clo Jure", 20))

  val pas = ps zip as //ps and as length should be same. if 'ps' size is 5 and 'as' size as 6, it will take first 5 elements for 'as'

  //ugly way
  println(pas map { tup =>
    val Person2(name,age) = tup._1
    val Address(street, city, country) = tup._2
    s"$name (age: $age) lives at $street, $city, in $country"
  })

  //Nicer way
  println(pas map {
    case (Person2(name, age), Address(street, city, country)) => s"$name (age: $age) lives at $street, $city, in $country"
  })

}

