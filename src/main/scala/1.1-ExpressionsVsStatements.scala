object ExpressionsVsStatements 

  def getNumberFive() : Int = 5

  val result = (getNumberFive() * 2, getNumberFive() * 2)

  //because of referential transparency, result is equivalent to result2
  val result2 = (10, 10)

  // but side effects break referential transparency
  def getNumberFive2(): Int = {
    println("5")
    5
  }

  val result3 = (getNumberFive2() * 2, getNumberFive2() * 2)

  // result3 is not equivalent to result4

  val ten =  getNumberFive2() * 2

  val result4 = (ten, ten) //


