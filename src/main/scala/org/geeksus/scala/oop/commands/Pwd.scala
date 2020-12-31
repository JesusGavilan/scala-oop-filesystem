package org.geeksus.scala.oop.commands
import org.geeksus.scala.oop.filesystem.State

class Pwd extends Command {
  override def apply(state: State): State =
    state.setMessage(state.wd.path)

}
