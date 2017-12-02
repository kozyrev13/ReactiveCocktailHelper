package dao

import javax.inject.Singleton
import javax.inject.{Inject, Singleton}

import models.Beverage
import models.Beverage
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

@Singleton()
class BeverageRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends BeverageTable with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def insert(beverage: Beverage): Future[Int] = db.run {
    beverageTableQueryInc += beverage
  }

  def insertAll(beverages: List[Beverage]): Future[Seq[Int]] = db.run {
    beverageTableQueryInc ++= beverages
  }

  def update(beverage: Beverage): Future[Int] = db.run {
    beverageTableQuery.filter(_.id === beverage.id).update(beverage)
  }

  def delete(id: Int): Future[Int] = db.run {
    beverageTableQuery.filter(_.id === id).delete
  }

  def getAll(): Future[List[Beverage]] = db.run {
    beverageTableQuery.to[List].result
  }

  def getById(empId: Int): Future[Option[Beverage]] = db.run {
    beverageTableQuery.filter(_.id === empId).result.headOption
  }

  def ddl = beverageTableQuery.schema

}

private[dao] trait BeverageTable { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  lazy protected val beverageTableQuery = TableQuery[BeverageTable]
  lazy protected val beverageTableQueryInc = beverageTableQuery returning beverageTableQuery.map(_.id)

  private[BeverageTable] class BeverageTable(tag: Tag) extends Table[Beverage](tag, "beverages") {
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    val title: Rep[String] = column[String]("title", O.SqlType("VARCHAR(200)"))
    val price: Rep[Int] = column[Int]("price", O.SqlType("INT"))

    def * = (title, price, id.?) <>(Beverage.tupled, Beverage.unapply)
  }

}