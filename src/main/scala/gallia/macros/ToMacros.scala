package gallia.macros

import scala.reflect.macros.blackbox.{ Context => BlackboxContext }

// ===========================================================================
object ToMacros {

  def toHead[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]): bbc.Tree = { import bbc.universe._
    q"""gallia.aobj(
          gallia.macros.toClsMacro     [${weakTypeTag[$TargetType].tpe}])(
          gallia.macros.dynamizerMacro [${weakTypeTag[$TargetType].tpe}].apply(${arg}))""" }

  // ---------------------------------------------------------------------------
  def toAObj[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]): bbc.Tree = { import bbc.universe._
    q"""gallia.aobj(
          gallia.macros.toClsMacro     [${weakTypeTag[$TargetType].tpe}])(
          gallia.macros.dynamizerMacro [${weakTypeTag[$TargetType].tpe}].apply(${arg}))""" }

  // ---------------------------------------------------------------------------
  def toObj[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]): bbc.Tree = { import bbc.universe._
    q"""gallia.macros.dynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply(${arg})""" }

  // ---------------------------------------------------------------------------
  def toCls[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]): bbc.Tree = { import bbc.universe._
    q"""gallia.macros.toClsMacro     [${weakTypeTag[$TargetType].tpe}]""" }

}

// ===========================================================================
