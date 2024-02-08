package gallia.macros

import scala.reflect.macros.blackbox.{ Context => BlackboxContext }

// ===========================================================================
object FromMacros {

  def fromHeadO[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.HeadO]): bbc.Tree /* $TargetType */ = {
      import bbc.universe._    
      q"""gallia.macros.undynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply(${arg}._forceResult.data)""" }  
  
    def fromHeadS[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.HeadS]): bbc.Tree /* List[$TargetType] */ = {
      import bbc.universe._    
      q"""${arg}._forceResult.data.consumeSelfClosing.map(gallia.macros.undynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply).toList""" }
  
  // ---------------------------------------------------------------------------
  // for good measure
  def fromAObj[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.AObj]): bbc.Tree /* $TargetType */ = {
      import bbc.universe._    
      q"""gallia.macros.undynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply(${arg}.data)""" }
  
    def fromAObjs[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.AObjs]): bbc.Tree /* List[$TargetType] */ = {
      import bbc.universe._    
      q"""${arg}.data.consumeSelfClosing.map(gallia.macros.undynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply).toList""" }

  // ---------------------------------------------------------------------------
  def fromObj[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.Obj]): bbc.Tree /* $TargetType */ = {
      import bbc.universe._    
      q"""gallia.macros.undynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply(${arg})""" }
    
    def fromObjs[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.Objs]): bbc.Tree /* List[$TargetType] */ = {
      import bbc.universe._    
      q"""${arg}.consumeSelfClosing.map(gallia.macros.undynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply).toList""" }

}

// ===========================================================================
