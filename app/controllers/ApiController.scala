// Make sure it's in the 'controllers' package
package controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, ControllerComponents}

import play.api.libs.json.Json
import repositories.DataRepository
import auth.AuthAction

@Singleton
class ApiController @Inject()(cc: ControllerComponents,
                              dataRepository: DataRepository,
                              authAction:AuthAction
                             )
  extends AbstractController(cc) {

  // Create a simple 'ping' endpoint for now, so that we
  // can get up and running with a basic implementation
  def ping = Action { implicit request =>
    Ok("Hello, Scala!")
  }

  // NEW - Get a single post
  def getPost(postId: Int) = authAction { implicit request =>
    dataRepository.getPost(postId) map { post =>
      // If the post was found, return a 200 with the post data as JSON
      Ok(Json.toJson(post))
    } getOrElse NotFound    // otherwise, return Not Found
  }

  // NEW - Get comments for a post
  def getComments(postId: Int) = Action { implicit request =>
    // Simply return 200 OK with the comment data as JSON.
    Ok(Json.toJson(dataRepository.getComments(postId)))
  }
}