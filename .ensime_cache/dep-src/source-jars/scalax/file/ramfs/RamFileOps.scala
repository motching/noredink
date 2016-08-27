/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2009-2010, Jesse Eichar          **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scalax.file
package ramfs

import scalax.io.StandardOpenOption.{Create,CreateFull,Read,Write,Truncate,DeleteOnClose,WriteTruncate,Append,CreateNew}
import java.io.{
  FileNotFoundException, IOException
}
import scalax.io.{Seekable, OpenOption}
import scalax.io.ResourceContext
import scalax.io.Resource

private[file] trait RamFileOps {
  self : RamPath =>
  private def fileResource[A](toResource:FileNode => A, openOptions: OpenOption*) = {
    val options = if(openOptions.isEmpty) List(Read,Write) else openOptions

    if((options contains CreateNew) && exists) {
      throw new IOException(this+" exists and therefore cannot be created new");
    }


    val nodeOption = fileSystem.lookup(this)

    if(nodeOption.isDefined) {
      val errors = options.distinct.flatMap{
        case Write | WriteTruncate | Append | Create | CreateNew | CreateFull | Truncate | DeleteOnClose  if !nodeOption.get.canWrite => List(Path.AccessModes.Write)
        case Read if !nodeOption.get.canRead => List(Path.AccessModes.Read)
        case _ => Nil
      }


        //_.toAccessModes).distinct.flatMap(mode => if(checkAccess(mode)) Nil else List(mode))

      if(errors.nonEmpty)
        throw new IOException(errors.mkString(", ") +" is not permitted for "+this)
    }
    nodeOption match {
      case Some(file:FileNode) =>
        toResource(file)
      case Some(d) =>
        throw new NotFileException(d.toString)
      case None if options exists {e => List(CreateNew, Create) contains e} =>
        createFile(false)
        fileSystem.lookup(this).collect {case f:FileNode => toResource(f)}.getOrElse {sys.error("file should have been created")}
      case None if options contains CreateFull =>
        createFile()
        fileSystem.lookup(this).collect {case f:FileNode => toResource(f)}.getOrElse {sys.error("file should have been created because of open option createFull")}
      case None =>
        throw new FileNotFoundException("No file found and will not create when open options are: " + openOptions)
    }

  }
  def inputStream = Resource.fromInputStream(fileResource(_.inputStream,Read)).updateContext(fileSystem.context)
  def outputStream(openOptions: OpenOption*) = Resource.fromOutputStream {
    val updatedOpts = openOptions match {
          case Seq() => WriteTruncate
          case opts if opts forall {opt => opt != Write && opt != Append} => openOptions :+ Write
          case _ => openOptions
      }
    fileResource( _.outputResource(this, updatedOpts:_*), updatedOpts:_*)
  }.updateContext(fileSystem.context)
  def channel(openOptions: OpenOption*) = Resource.fromSeekableByteChannel{
    val updatedOpts = openOptions match {
      case Seq() => WriteTruncate
      case opts if opts forall {opt => opt != Write && opt != Append} => openOptions :+ Write
      case _ => openOptions
    }
    fileResource(_.channel(this, updatedOpts:_*), updatedOpts:_*)
  }.updateContext(fileSystem.context)
  def fileChannel(openOptions: OpenOption*) = None // not supported

  def withLock[R](start: Long,size: Long,shared: Boolean, context:ResourceContext)(block: (Seekable) => R):Option[R] = None // TODO
}
