package gallia
package macros

import aptus._

import scala.reflect.api.Universe
import gallia.reflect._

// ===========================================================================
case class ClassTraversal(values: Seq[Cls]) {      
    def topologicalTraversal: Seq[Cls] = values.reverse } // by design (constructed by traversal)

  // ===========================================================================
  object ClassTraversal {

    def parse[$TargetType : universe.WeakTypeTag](universe: Universe): ClassTraversal =
      universe
        .weakTypeTag[$TargetType]
        .tpe
        .pipe(gallia.reflect.lowlevel.TypeLeafParserRuntime2._parseTypeNode)
        .pipe(rec)
        .distinct // in case the same class is referenced multiple times
        .pipe(ClassTraversal.apply)
    
    // ===========================================================================
    private def rec(node: TypeNode): Seq[Cls] = {
      val leaf = 
        node
          .validContainerOpt
          .getOrElse(node.leaf)

      // ---------------------------------------------------------------------------
      val nestedClasses: Seq[Cls] =
        leaf
          .fields
          .flatMap { _.typeNode.in.someIf(_.isContainedDataClass) }
          .flatMap(rec)

      // ---------------------------------------------------------------------------
      leaf.forceDataClass /* TODO: t210326131124 - wrap errors */ +: nestedClasses
    }

}

// ===========================================================================
