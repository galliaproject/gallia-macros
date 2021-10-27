package gallia.macros

import aptus._

import scala.reflect.macros.blackbox.{Context => BlackboxContext}
import scala.reflect.runtime.{universe => RuntimeUniverse}
import scala.reflect.api.Universe
import enumeratum.{Enum, EnumEntry}
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
        .thn(gallia.reflect.TypeNode.parse)
        .thn(rec)
        .distinct // in case the same class is referenced multiple times
        .thn(ClassTraversal.apply)     
    
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
          .flatMap { _.node.in.someIf(_.isContainedDataClass) }
          .flatMap(rec)

      // ---------------------------------------------------------------------------
      leaf.forceDataClass /* TODO: t210326131124 - wrap errors */ +: nestedClasses
    }

}

// ===========================================================================
