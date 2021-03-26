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
        macro _toHeadMacro[$TargetType]
          def _toHeadMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]) : bbc.Tree =
            ToMacros.toHead[$TargetType](bbc)(arg)

    // ---------------------------------------------------------------------------
    def toAObjMacro[$TargetType](arg: $TargetType): gallia.AObj =
        macro _toAObjMacro[$TargetType]
          def _toAObjMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]) : bbc.Tree =
            ToMacros.toAObj[$TargetType](bbc)(arg)
  
    // ---------------------------------------------------------------------------
    def toObjMacro[$TargetType](arg: $TargetType): gallia.Obj =
        macro _toObjMacro[$TargetType]
          def _toObjMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]) : bbc.Tree =
            ToMacros.toObj[$TargetType](bbc)(arg)
  
    // ---------------------------------------------------------------------------
    def toClsMacro[$TargetType](arg: $TargetType): gallia.Cls =
        macro _toClsMacro1[$TargetType]
          def _toClsMacro1[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]) : bbc.Tree =
            ToMacros.toCls[$TargetType](bbc)(arg)

    // ---------------------------------------------------------------------------
    def toClsMacro[$TargetType]: Cls =
        macro _toClsMacro2[$TargetType]
          def _toClsMacro2[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext): bbc.Tree = 
            SchematizingMacro[$TargetType](bbc)  

  // ===========================================================================
  // from

  def fromHeadMacro[$TargetType](arg: gallia.HeadO): $TargetType =
        macro _undynamizer4[$TargetType]
          def _undynamizer4[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.HeadO]): bbc.Tree = 
            FromMacros.fromHead[$TargetType](bbc)(arg)
    
    // ---------------------------------------------------------------------------
    def fromAObjMacro[$TargetType](arg: gallia.AObj): $TargetType =
        macro _fromAObjMacro[$TargetType]
          def _fromAObjMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.AObj]): bbc.Tree = 
            FromMacros.fromAObj[$TargetType](bbc)(arg)

    // ---------------------------------------------------------------------------
    def fromObjMacro[$TargetType](arg: gallia.Obj): $TargetType =
        macro _fromObjMacro[$TargetType]
          def _fromObjMacro[$TargetType : bbc.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.Obj]): bbc.Tree = 
            FromMacros.fromObj[$TargetType](bbc)(arg)

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
