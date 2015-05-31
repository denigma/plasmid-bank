package club.diybio.bank.views

import org.denigma.binding.binders.extractors.EventBinding
import org.denigma.binding.extensions._
import org.denigma.binding.views.BindableView
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.ext.{Ajax, AjaxException}
import org.scalajs.dom.raw.HTMLElement
import rx.ops._
import rx.{Rx, Var}

import scala.collection.immutable._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.util.{Failure, Success}

/**
 * Login view
 */
class LoginView(val elem:HTMLElement, val params:Map[String,Any]) extends BindableView with Login with Registration with Signed
{


  isSigned.handler {
    if(isSigned.now)  inRegistration() = false
  }

  val emailLogin = Rx{  login().contains("@")} //true when login field contains email

  /**
   * If anything has changed
   */
  val anyChange = Rx{ (login(),password(),email(),repeat(),inLogin())}

  /**
   * Handler for anychange
   */
  val clearMessage = anyChange.handler{
    message()="" //cleans error/warning message
  }

  /**
   * macro that extracts Rx and Var-s to make them accessible for html binding
   */
  override def activateMacro(): Unit = { extractors.foreach(_.extractEverything(this))}

  /**
   * Here we use default binders so we can bind to Strings, Booleans and Double-s
   * It also supports some binding logic like: data-show-if, data-class-if and so on...
   */
  override protected def attachBinders(): Unit = binders =  BindableView.defaultBinders(this)

}

trait Signed extends Registration {


  lazy val neutralColor = "blue"

  val onLogout = logoutClick.takeIf(this.isSigned)

  def logOut()  = Ajax.get(sq.h(s"users/logout?username=${this.login.now}&password=${this.password.now}"))

  /**
   * Logout button handler
   */
  val logoutHandler = onLogout.handler{
    this.logOut().onComplete{
      case Success(req) =>
        Session.logout()
        this.clearAll()


      case Failure(ex:AjaxException) =>
        //this.report(s"logout failed: ${ex.xhr.responseText}")
        this.report(ex.xhr)


      case _ => this.reportError("unknown failure")

    }
  }

  /**
   * Clears everything
   */
  def clearAll() = {
    this.inRegistration()=false
    this.login() = ""
    this.password()=""
    this.repeat()=""
    this.email()=""
  }
  val signupClass: Rx[String] =  Rx{
    if(this.inRegistration())
      if(this.canRegister()) "positive" else neutralColor
    else
      "basic"
  }

  /**
   * Positive (green) color if we can try to login and neutral otherwise
   */
  val loginClass: Rx[String] = Rx{
    if(this.inLogin())
      if(this.canLogin()) "positive" else neutralColor
    else
      "basic"
  }
}

/**
 * Deals with login features
 */
trait Login extends BasicLogin{


  /**
   * When the user decided to switch to login
   */
  val loginToggleClick = loginClick.takeIf(inRegistration)

  /**
   * When the user comes from registration to login
   */
  val toggleLogin = this.loginToggleClick.handler{
    this.inRegistration() = false
  }

  def auth() = Ajax.get(sq.h(s"users/login?username=${this.login.now}&password=${this.password.now}"))
  val authClick = loginClick.takeIfAll(canLogin,inLogin)
  val authHandler = authClick.handler{
    this.auth().onComplete{

      case Success(req) =>
        //dom.alert("authed successfuly")
        Session.login(login.now)
      //TODO: get full username
      //Session.setUser(user)


      case Failure(ex:AjaxException) =>
        //this.report(s"Authentication failed: ${ex.xhr.responseText}")
        this.report(ex.xhr)


      case _ => this.reportError("unknown failure")
    }
  }


}

/**
 * Part of the view that deals with registration
 */
trait Registration extends BasicLogin{

  /**
   * rx property binded to repeat password input
   */
  val repeat = Var("")

  //Rx that is true if email input field contains valid email text
  val emailValid: Rx[Boolean] = Rx {email().length>4 && this.isValid(email())}

  /**
   * Email regex to check if email is valid
   * @param email
   * @return
   */
  def isValid(email: String): Boolean = """(\w+)@([\w\.]+)""".r.unapplySeq(email).isDefined


  /**
   * True if password and repeatpassword match
   */
  val samePassword = Rx{
    password()==repeat()
  }
  /**
   * Reactive variable telling if register request can be send
   */
  val canRegister = Rx{ samePassword() && canLogin() && emailValid()}

  val toggleRegisterClick = this.signupClick.takeIf(this.inLogin)
  val toggleRegisterHandler = this.toggleRegisterClick.handler{
    this.inRegistration() = true
  }

  protected def register() =  Ajax.get(sq.h(s"users/register?username=${this.login.now}&password=${this.password.now}&email=${this.email.now}"))


  val registerClick = this.signupClick.takeIfAll(this.canRegister,this.inRegistration)
  val registerHandler = this.registerClick.handler{
    this.register().onComplete{

      case Success(req) =>

        Session.login(login.now)
      //TODO: get full username
      //Session.setUser(user)

      case Failure(ex:AjaxException) =>
        //this.report(s"Registration failed: ${ex.xhr.responseText}")
        this.report(ex.xhr)

      case _ => this.reportError("unknown failure")

    }
  }


}

/**
 * Basic login variables/events
 * to understand what is happening here it is recommended to read docs on ScalaRx: https://github.com/lihaoyi/scala.rx
 */
trait BasicLogin extends BindableView
{
  /**
   * Extracts name from global
   */
  val registeredName = Session.username //I know, that it is bad to have shared mustable state=)

  val login = Var("") //reactive variable that binds to Login Input field
  val password = Var("") //reactive variable that binds to password Input field
  val email = Var("") //reactive variable that binds to password Input field
  val message = Var("") //reactive variable that provides messages


  val isSigned = Session.currentUser.map(_.isDefined) // is true when user is signed
  val inRegistration = Var(false) // Var that switches between registration and login modes
  val inLogin = Rx(!inRegistration() && !isSigned()) // Rx that tells us if user is in login mode

  /**
   * Rx that is true when username input field has valid text value
   */
  val validUsername:Rx[Boolean] = login.map(l=>l.length>4 && l.length<20 && !l.contains(" ") && l!="guest")
  val validPassword:Rx[Boolean] = password.map(p=>p.length>4 && p!=login.now) //valid password

  val canLogin = Rx{validUsername() && validPassword()} //Rx that is true if we can try to login

  val loginClick: Var[MouseEvent] = Var(EventBinding.createMouseEvent())  //binds to Login button
  val logoutClick: Var[MouseEvent] = Var{EventBinding.createMouseEvent()} //binds to Logout button
  val signupClick: Var[MouseEvent] = Var(EventBinding.createMouseEvent()) //binds to Signup button

  /**
   * Function for error reporting from xmlhttp request
   * @param req xmlhttp request
   * @return message string
   */
  def report(req:org.scalajs.dom.XMLHttpRequest): String = req.response.dyn.message match {
    case m if m.isNullOrUndef => this.report(req.responseText)
    case other =>this.report(other.toString)
  }

  /**
   * Reports some info
   * @param str
   * @return
   */
  def report(str:String): String = {
    this.message()=str
    str
  }

  def reportError(str:String) = dom.console.error(this.report(str))

}

/**
 * Global object to store username of current User
 */
object Session {

  val currentUser:Var[Option[String]]= Var(None)

  def login(str:String) = str match {
    case "" | null | "guest" => Session.currentUser.set(None)
    case uname => Session.currentUser.set(Some(uname))
  }

  def logout() = currentUser.set(None)

  val username = currentUser.map{
    case Some(str) => localName(str)
    case None=> "guest"
  }



  def localName(str:String): String = if(str.endsWith("/"))
    localName(str.substring(0,str.length-2))
  else
  if(str.contains("/"))
    str.substring(str.lastIndexOf("/")+1)
  else str


}
