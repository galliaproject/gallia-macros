package gallia.macros

import scala.reflect.macros.blackbox.{ Context => BlackboxContext }

// ===========================================================================
object ToMacros {

  def toHeadO[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]): bbc.Tree /* HeadO */ = {
      import bbc.universe._

      q"""gallia.aobj(
          gallia.macros.toClsMacro     [${weakTypeTag[$TargetType].tpe}])(
          gallia.macros.dynamizerMacro [${weakTypeTag[$TargetType].tpe}].apply(${arg}))""" }

    def toHeadS[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[Traversable[$TargetType]]): bbc.Tree /* HeadS */ = {
        import bbc.universe._

        q"""gallia.aobjs(
                       gallia.macros.toClsMacro     [${weakTypeTag[$TargetType].tpe}])(
            ${arg}.map(gallia.macros.dynamizerMacro [${weakTypeTag[$TargetType].tpe}].apply): _*)""" }

  // ---------------------------------------------------------------------------
  def toAObj[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]): bbc.Tree  /* AObj */ = {
      import bbc.universe._

      q"""gallia.aobj(
          gallia.macros.toClsMacro    [${weakTypeTag[$TargetType].tpe}])(
          gallia.macros.dynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply(${arg}))""" }

    def toAObjs[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[Traversable[$TargetType]]): bbc.Tree /* AObjs */ = {
      import bbc.universe._

      q"""gallia.aobjs(
                     gallia.macros.toClsMacro     [${weakTypeTag[$TargetType].tpe}])(
          ${arg}.map(gallia.macros.dynamizerMacro [${weakTypeTag[$TargetType].tpe}].apply): _*)""" }

  // ---------------------------------------------------------------------------
  def toObj[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[$TargetType]): bbc.Tree /* Obj */ = { 
      import bbc.universe._
        q"""                     gallia.macros.dynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply(${arg})""" }

    def toObjs[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[Traversable[$TargetType]]): bbc.Tree /* Objs */ = {
      import bbc.universe._  
        q"""Objs.from(${arg}.map(gallia.macros.dynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply))""" }

  // ---------------------------------------------------------------------------
  def toCls1[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(ignored: bbc.Expr[$TargetType]): bbc.Tree /* Cls */ = {
      import bbc.universe._
        q"""gallia.macros.toClsMacro[${weakTypeTag[$TargetType].tpe}]""" }

    def toCls2[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(ignored: bbc.Expr[Traversable[$TargetType]]): bbc.Tree /* Cls */ = {
      import bbc.universe._
        q"""gallia.macros.toClsMacro[${weakTypeTag[$TargetType].tpe}]""" }

}

// ===========================================================================
