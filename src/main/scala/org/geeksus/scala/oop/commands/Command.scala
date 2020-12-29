package org.geeksus.scala.oop.commands

import org.geeksus.scala.oop.filesystem.State

trait Command {
  def apply(state: State): State
}

object Command {
  def from(input: String): Command =
    new UnknownCommand
}
