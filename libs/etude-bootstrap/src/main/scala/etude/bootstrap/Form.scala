package etude.bootstrap

import scala.xml.Elem

object Form {
  def form(action: String, csrfToken: String, v: Any): Elem = {
    <form action={action} method="POST" class="form" role="form"><input type="hidden" name="csrfToken" value={csrfToken}/>{v}</form>
  }

  def formGroup(v: Any): Elem = <div class="form-group">{v}</div>

  def inputText(name: String, placeholder: String): Elem = <input type="text" name={name} placeholder={placeholder} class="form-control"/>
  def inputEmail(name: String, placeholder: String): Elem = <input type="email" name={name} placeholder={placeholder} class="form-control"/>
  def inputPassword(name: String, placeholder: String): Elem = <input type="password" name={name} placeholder={placeholder} class="form-control"/>
}
