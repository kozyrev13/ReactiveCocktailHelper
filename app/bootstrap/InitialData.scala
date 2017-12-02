package bootstrap

import com.google.inject.Inject
import javax.inject.Singleton

import java.util.Date

import dao.CocktailRepository
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.Logger

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class InitialData @Inject() (cocktailRepository: CocktailRepository) {

  def insert = for {
    ccktls <- cocktailRepository.getAll() if (ccktls.length == 0)
    _ <- cocktailRepository.insertAll(Data.cocktails)
  } yield {}

  try {
    Logger.info("DB initialization.................")
    Await.result(insert, Duration.Inf)
  } catch {
    case ex: Exception =>
      Logger.error("Error in database initialization ", ex)
  }

}

object Data {
  val cocktails = List(

  )
}
