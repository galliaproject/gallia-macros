package gallia

import scala.util.chaining._

import scala.reflect.api.Universe
import scala.reflect.runtime.{universe => RuntimeUniverse}
import scala.reflect.macros.blackbox.{Context => BlackboxContext}

import aptus._

// ===========================================================================
package object macros {
  import scala.language.experimental.macros  

  // ---------------------------------------------------------------------------
  private[macros] val InstanceVariableName = "__instance" // arbitrary
    
  // ===========================================================================
  def schematizeRuntime[$TargetType : RuntimeUniverse.WeakTypeTag](): Cls = 
    ClassTraversal.parse(RuntimeUniverse).values.head

  // ===========================================================================
  // functions (better for reuse)

  def dynamizerMacro[$TargetType]: $TargetType => Obj =
      macro _dynamizerMacro[$TargetType]
        def _dynamizerMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext): bbc.Tree =
          DynamizerMacro[$TargetType](bbc)

    // ---------------------------------------------------------------------------    
    def undynamizerMacro[$TargetType]: Obj => $TargetType =
      macro _undynamizerMacro[$TargetType]
        def _undynamizerMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext): bbc.Tree = 
          UndynamizerMacro[$TargetType](bbc)

  // ===========================================================================
  // to

  def toHeadMacro[$TargetType](arg: $TargetType): gallia.HeadO =
        macro _toHeadOMacro[$TargetType]
          def _toHeadOMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]) : bbc.Tree =
            ToMacros.toHeadO[$TargetType](bbc)(arg)

    def toHeadMacro[$TargetType](arg: Traversable[$TargetType]): gallia.HeadS =
        macro _toHeadSMacro[$TargetType]
          def _toHeadSMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[Traversable[$TargetType]]) : bbc.Tree =
            ToMacros.toHeadS[$TargetType](bbc)(arg)

    // ---------------------------------------------------------------------------
    def toAObjMacro[$TargetType](arg: $TargetType): gallia.AObj =
        macro _toAObjMacro[$TargetType]
          def _toAObjMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]) : bbc.Tree =
            ToMacros.toAObj[$TargetType](bbc)(arg)

      def toAObjsMacro[$TargetType](arg: Traversable[$TargetType]): gallia.AObjs =
        macro _toAObjsMacro[$TargetType]
          def _toAObjsMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[Traversable[$TargetType]]) : bbc.Tree =
            ToMacros.toAObjs[$TargetType](bbc)(arg)

    // ---------------------------------------------------------------------------
    def toObjMacro[$TargetType](arg: $TargetType): gallia.Obj =
        macro _toObjMacro[$TargetType]
          def _toObjMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]) : bbc.Tree =
            ToMacros.toObj[$TargetType](bbc)(arg)

      def toObjsMacro[$TargetType](arg: Traversable[$TargetType]): gallia.Objs =
        macro _toObjsMacro[$TargetType]
          def _toObjsMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[Traversable[$TargetType]]) : bbc.Tree =
            ToMacros.toObjs[$TargetType](bbc)(arg)

    // ---------------------------------------------------------------------------
    def toClsMacro[$TargetType](arg: $TargetType): gallia.Cls =
        macro _toClsMacro1[$TargetType]
          def _toClsMacro1[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]) : bbc.Tree =
            ToMacros.toCls1[$TargetType](bbc)(arg)

      def toClsMacro[$TargetType](arg: Traversable[$TargetType]): gallia.Cls =
        macro _toClsMacro2[$TargetType]
          def _toClsMacro2[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[Traversable[$TargetType]]) : bbc.Tree =
            ToMacros.toCls2[$TargetType](bbc)(arg)
              
    // ===========================================================================
    def toClsMacro[$TargetType]: Cls =
        macro _toClsMacro3[$TargetType]
          def _toClsMacro3[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext): bbc.Tree = 
            SchematizingMacro[$TargetType](bbc)  

  // ===========================================================================
  // from

  def fromHeadMacro[$TargetType](arg: gallia.HeadO): $TargetType =
        macro _fromHeadOMacro[$TargetType]
          def _fromHeadOMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.HeadO]): bbc.Tree = 
            FromMacros.fromHeadO[$TargetType](bbc)(arg)

  def fromHeadMacro[$TargetType](arg: gallia.HeadS): List[$TargetType] =
        macro _fromHeadSMacro[$TargetType]
          def _fromHeadSMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.HeadS]): bbc.Tree = 
            FromMacros.fromHeadS[$TargetType](bbc)(arg)

    // ---------------------------------------------------------------------------
    def fromAObjMacro[$TargetType](arg: gallia.AObj): $TargetType =
        macro _fromAObjMacro[$TargetType]
          def _fromAObjMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.AObj]): bbc.Tree = 
            FromMacros.fromAObj[$TargetType](bbc)(arg)

    def fromAObjsMacro[$TargetType](arg: gallia.AObjs): List[$TargetType] =
        macro _fromAObjsMacro[$TargetType]
          def _fromAObjsMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.AObjs]): bbc.Tree = 
            FromMacros.fromAObjs[$TargetType](bbc)(arg)
          
    // ---------------------------------------------------------------------------
    def fromObjMacro[$TargetType](arg: gallia.Obj): $TargetType =
        macro _fromObjMacro[$TargetType]
          def _fromObjMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.Obj]): bbc.Tree = 
            FromMacros.fromObj[$TargetType](bbc)(arg)

    def fromObjsMacro[$TargetType](arg: gallia.Objs): List[$TargetType] =
        macro _fromObjsMacro[$TargetType]
          def _fromObjsMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.Objs]): bbc.Tree = 
            FromMacros.fromObjs[$TargetType](bbc)(arg)

  // ===========================================================================
  private[macros] type ClassName    = String
  private[macros] type FieldName    = String  
  private[macros] type EnumName     = String
  private[macros] type EnumValue    = String
  private[macros] type VariableName = String

  // ---------------------------------------------------------------------------
  type Cls = gallia.meta.Cls
  val  Cls = gallia.meta.Cls
  
  type Fld = gallia.meta.Fld
  val  Fld = gallia.meta.Fld

  type Info  = gallia.meta.Info 
  val  Info  = gallia.meta.Info
  
  type Container = gallia.reflect.Container
  val  Container = gallia.reflect.Container

  type Containee = gallia.meta.Containee
  
  type BasicType = gallia.reflect.BasicType
  val  BasicType = gallia.reflect.BasicType

  // ===========================================================================  
  private[macros] def formatVariableName(name: ClassName, suffix: String): VariableName =
    s"`${(name.head.toLower +: name.tail)}${suffix}`"
 
}

// ===========================================================================
