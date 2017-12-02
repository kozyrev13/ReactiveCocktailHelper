package models


case class Cocktail(title: String, description: String, id: Option[Int]=None)
case class Beverage(title: String, price: Int,id: Option[Int]=None)