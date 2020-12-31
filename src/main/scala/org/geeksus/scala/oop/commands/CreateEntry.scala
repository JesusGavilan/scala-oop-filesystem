package org.geeksus.scala.oop.commands

import org.geeksus.scala.oop.files.{DirEntry, Directory}
import org.geeksus.scala.oop.filesystem.State

abstract class CreateEntry(name: String) extends Command {


  override def apply(state: State): State = {

    val wd = state.wd
    if (wd.hasEntry(name)) {
      state.setMessage("Entry " + name + " already exists!")
    } else if (name.contains(Directory.SEPARATOR)) {
      state.setMessage(name + " must not containe separators!")
    } else if (checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name!")
    } else {
      doCreateEntry(state, name)
    }
  }

  def checkIllegal(str: String): Boolean  = {
    name.contains(".")
  }

  def doCreateEntry(state: State, name: String): State = {

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      /*
        someDir
        /a
        /b
        (new) /d
        => new someDir
        /a
        /b
        /d

        /a/b
          /c
          /d
          (new) /e

        new /a
        new /b (parent /a)
          /c
          /d
          /e
       */
      if (path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        /*
        /a/b
          /c
          /d
           (new entry)
        currentDirectory = /a
        path = ["/b"]
        */
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }

    }

    val wd = state.wd
    val fullPath = wd.path

    //1. all the directory in the full path
    val allDirsInPath = wd.getAllFoldersInPath

    //2. create new directory entry in the wd
    val newEntry: DirEntry = createSpecificEntry(state)
    //val newDir = Directory.empty(wd.path, name)
    //3. update the whole directory structure starting from the root
    // (the directory structure is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)
    //4. find new working directory INSTANCE given wd's full path, in the NEW directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }

  def createSpecificEntry(state: State): DirEntry

}
