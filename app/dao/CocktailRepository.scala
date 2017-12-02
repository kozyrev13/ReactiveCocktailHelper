package dao

import javax.inject.Singleton
import javax.inject.{Inject, Singleton}

import models.{Beverage, Cocktail}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

@Singleton()
class CocktailRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends CocktailTable with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def insert(cocktail: Cocktail): Future[Int] = db.run {
    cocktailTableQueryInc += cocktail
  }

  def insertAll(cocktails: List[Cocktail]): Future[Seq[Int]] = db.run {
    cocktailTableQueryInc ++= cocktails
  }

  def update(cocktail: Cocktail): Future[Int] = db.run {
    cocktailTableQuery.filter(_.id === cocktail.id).update(cocktail)
  }

  def delete(id: Int): Future[Int] = db.run {
    cocktailTableQuery.filter(_.id === id).delete
  }

  def getAll(): Future[List[Cocktail]] = db.run {
    cocktailTableQuery.to[List].result
  }

  def getById(empId: Int): Future[Option[Cocktail]] = db.run {
    cocktailTableQuery.filter(_.id === empId).result.headOption
  }

  def ddl = cocktailTableQuery.schema

}

private[dao] trait CocktailTable { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  lazy protected val cocktailTableQuery = TableQuery[CocktailTable]
  lazy protected val cocktailTableQueryInc = cocktailTableQuery returning cocktailTableQuery.map(_.id)

  private[CocktailTable] class CocktailTable(tag: Tag) extends Table[Cocktail](tag, "cocktails") {
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    val title: Rep[String] = column[String]("title", O.SqlType("VARCHAR(200)"))
    val description: Rep[String] = column[String]("description", O.SqlType("VARCHAR(200)"))

    def * = (title, description, id.?) <>(Cocktail.tupled, Cocktail.unapply)
  }

}
