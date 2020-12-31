package org.geeksus.scala.oop.commands
import org.geeksus.scala.oop.files.{DirEntry, Directory}
import org.geeksus.scala.oop.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {

  override def apply(state: State): State = {
    /*

    cd /something/somethingElse/.../
    cd a/b/c = relative to the currenbt working directory

    cd ..
    cd .
    cd a/./.././a/
     */

    //1. find root
    val root: Directory = state.root
    val wd: Directory = state.wd
    //2. find the absolut path of the directory I want to cd to
    val absolutePath: String = {
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else if (wd.isRoot) wd.path + dir
      else wd.path + Directory.SEPARATOR + dir
    }
    //3. find the directory to cd to, given the path
    val destinationDirectory: DirEntry = doFindEntry(root, absolutePath)
    //4. change the state fiven the new directory
    if (destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(dir + ": no such directory")
    else
      State(root, destinationDirectory.asDirectory)
  }

  def doFindEntry(root: Directory, path: String): DirEntry = {
    @tailrec
    def findEntryHelper(currentDirectory: Directory,path: List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDir: DirEntry = currentDirectory.findEntry(path.head)
        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    //1. tokens
    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    //2. navigate to the correct entry
    findEntryHelper(root, tokens)
  }
}
