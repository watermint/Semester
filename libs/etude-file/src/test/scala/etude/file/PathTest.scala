package etude.file

import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import java.nio.file.FileSystems

/**
 *
 */
@RunWith(classOf[JUnitRunner])
class PathTest extends Specification {
  lazy val testHomePath = FileSystems.getDefault.getPath("libs/etude-file/src/test/resources/etude/file")

  "Path" should {
    "Create correct instances" in {
      Path(testHomePath.resolve("EmptyFile")) must beAnInstanceOf[File]
      Path(testHomePath.resolve("EmptyDir")) must beAnInstanceOf[Dir]
      Path(testHomePath.resolve("SymlinkToEmptyFile")) must beAnInstanceOf[Symlink]
      Path(testHomePath.resolve("NoExistent")) must beAnInstanceOf[NoExistent]
    }
  }

  "Directory" should {
    "List files" in {
      Dir(testHomePath.resolve("RegularDir")).children.get.length must equalTo(5)
    }
  }

  "Symlink" should {
    "Point to correct instance" in {
      Symlink(testHomePath.resolve("SymlinkToEmptyFile")).target.get must equalTo(Path(testHomePath.resolve("EmptyFile")))
      Symlink(testHomePath.resolve("SymlinkToEmptyDir")).target.get must equalTo(Path(testHomePath.resolve("EmptyDir")))
    }
  }
}
