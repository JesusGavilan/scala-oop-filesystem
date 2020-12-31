package org.geeksus.scala.oop.commands
import org.geeksus.scala.oop.files.{DirEntry, Directory}
import org.geeksus.scala.oop.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}
