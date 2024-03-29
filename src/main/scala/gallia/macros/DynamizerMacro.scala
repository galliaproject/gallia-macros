package gallia.macros

import scala.reflect.macros.blackbox.{ Context => BlackboxContext }

// ===========================================================================
object DynamizerMacro {    
  private val ConstructorMethodName = "obj" // TODO: how to not hardcode name like this?  

  // ===========================================================================
  def apply[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext): bbc.Tree = { import bbc.universe._

    val subTrees: Seq[(ClassName, bbc.Tree)] =
      ClassTraversal
        .parse[$TargetType](bbc.universe)
        .topologicalTraversal
        .map { dynamizeClass(_)(bbc) }

    // ---------------------------------------------------------------------------
    val methodTrees: Seq[bbc.Tree] =
      subTrees
        .map { case (className, subTree) =>
          // eg: def addressDynamizer: Function[Address, Obj] = ...
          q"""def ${TermName(formatVariableName(className))}: Function[${TypeName(className)}, ${typeOf[gallia.Obj]}] = ${subTree}"""}

    // ---------------------------------------------------------------------------
    q"""
      ..${methodTrees}
      
      ${TermName(formatVariableName(subTrees.last._1))}
    """
  }

  // ===========================================================================
  private def dynamizeClass(c: Cls)(bbc: BlackboxContext): Tuple2[ClassName, bbc.Tree] = { import bbc.universe._        
    val entryTrees: Seq[Tree] =
      c .fields
        .map { field =>
          q"Tuple2[Symbol, Any](${field.key}, ${valueTree(field)(bbc)})" }

    // ---------------------------------------------------------------------------
    c.forceName ->
      q"""        
        new Function[${TypeName(c.forceName)}, ${typeOf[gallia.Obj]}] {
          def apply(${TermName(InstanceVariableName)}: ${TypeName(c.forceName)}): ${typeOf[gallia.Obj]} =
            gallia.obj(..${entryTrees}) }"""
  }

  // ===========================================================================
  private def valueTree(field: Fld)(bbc: BlackboxContext): bbc.Tree = { import bbc.universe._
    val instanceVariable = TermName(InstanceVariableName)
    val accessorMethod   = TermName(field.skey)

    // ---------------------------------------------------------------------------    
    field.subInfo1.valueType match {
    
      case c: Cls =>
        val subDynamizerVariable = TermName(formatVariableName(c.forceName))
        
        field.info.container1 match {
          case Container._One => q"${subDynamizerVariable}.apply(${instanceVariable}.${accessorMethod})"                                            //               T
          case Container._Opt => q"                              ${instanceVariable}.${accessorMethod}.map(      ${subDynamizerVariable}.apply)"    //        Option[T]
          case Container._Nes => q"                              ${instanceVariable}.${accessorMethod}.map(      ${subDynamizerVariable}.apply)"    //        Seq   [T]
          case Container._Pes => q"                              ${instanceVariable}.${accessorMethod}.map(_.map(${subDynamizerVariable}.apply))" } // Option[Seq   [T]]

      // ---------------------------------------------------------------------------
      case _: _Enm      =>       q"gallia.EnumValue(             ${instanceVariable}.${accessorMethod}.entryName)"
      case _: BasicType =>       q"                              ${instanceVariable}.${accessorMethod}"
    }
  }

  // ===========================================================================
  private def formatVariableName(name: ClassName): VariableName = gallia.macros.formatVariableName(name, suffix = "Dynamizer")
  
}

// ===========================================================================
