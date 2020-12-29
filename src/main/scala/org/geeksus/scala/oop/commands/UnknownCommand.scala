package org.geeksus.scala.oop.commands
import org.geeksus.scala.oop.filesystem.State

class UnknownCommand extends Command {

  override def apply(state: State): State =
    state.setMessage("Command not found!")
}
