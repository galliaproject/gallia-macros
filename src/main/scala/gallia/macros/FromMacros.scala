package gallia.macros

import scala.reflect.macros.blackbox.{ Context => BlackboxContext }

// ===========================================================================
object FromMacros {

  def fromHead[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.HeadO]): bbc.Tree = {
    import bbc.universe._    
    q"""gallia.macros.undynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply(${arg}.forceAObj.data)""" }  
    
  // ---------------------------------------------------------------------------
  def fromAObj[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.AObj]): bbc.Tree = { // for good measure
    import bbc.universe._    
    q"""gallia.macros.undynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply(${arg}.data)""" }
  
  // ---------------------------------------------------------------------------
  def fromObj[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext)(arg: bbc.Expr[gallia.Obj]): bbc.Tree = {
    import bbc.universe._    
    q"""gallia.macros.undynamizerMacro[${weakTypeTag[$TargetType].tpe}].apply(${arg})""" }  

  // ---------------------------------------------------------------------------
  def fromCls[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext): bbc.Tree = {
    import bbc.universe._    
    q"""classOf[gallia.macros.toClsMacro[${weakTypeTag[$TargetType].tpe}]]""" }  

}

// ===========================================================================
