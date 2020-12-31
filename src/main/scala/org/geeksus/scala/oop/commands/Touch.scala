package org.geeksus.scala.oop.commands
import org.geeksus.scala.oop.files.{DirEntry, File}
import org.geeksus.scala.oop.filesystem.State

class Touch(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}
