package exercices

// Given the following interface for a shopping cart write some properties that you would expect to hold for any implementation of this interface. 
// Express these properties as laws 
// Write a simple implementation of the shopping cart and check that it satisfies the laws you have defined.

trait ShoppingCart[A]:
  def add(item: A): ShoppingCart[A]
  def remove(item: A): ShoppingCart[A]
  def items: List[A]
  def total: BigDecimal
