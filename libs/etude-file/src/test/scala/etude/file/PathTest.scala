package etude.file

import java.nio.file.FileSystems
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PathTest extends Specification {
  lazy val testHomePath = FileSystems.getDefault.getPath("libs/etude-file/src/test/resources/etude/file")

  "Path" should {
    "Create correct instances" in {
      Path(testHomePath.resolve("EmptyFile")) must beAnInstanceOf[File]
      Path(testHomePath.resolve("EmptyDir")) must beAnInstanceOf[Dir]
//      Path(testHomePath.resolve("SymlinkToEmptyFile")) must beAnInstanceOf[Symlink]
      Path(testHomePath.resolve("NoExistent")) must beAnInstanceOf[NoExistent]
    }
  }

  "Directory" should {
    "List files" in {
      Dir(testHomePath.resolve("RegularDir")).children.right.get.length must equalTo(5)
    }
  }

  // Skip due to project structure change
//  "Symlink" should {
//    "Point to correct instance" in {
//      Symlink(testHomePath.resolve("SymlinkToEmptyFile")).target.right.get must equalTo(Path(testHomePath.resolve("EmptyFile")))
//      Symlink(testHomePath.resolve("SymlinkToEmptyDir")).target.right.get must equalTo(Path(testHomePath.resolve("EmptyDir")))
//    }
//  }
}
