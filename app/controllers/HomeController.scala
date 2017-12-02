package controllers

import javax.inject._

import scala.concurrent.ExecutionContext.Implicits.global

import dao.CocktailRepository
import play.api.Logger
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(rep: CocktailRepository) extends Controller {

  val logger = Logger(this.getClass())
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready.",Nil))

  }
  def list() = Action.async {
    rep.getAll().map { res =>
      logger.info("Emp list: " + res)
      Ok(views.html.index("Your new application is ready.",res))
    }
  }

}
