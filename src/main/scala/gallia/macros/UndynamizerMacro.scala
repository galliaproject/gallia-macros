package gallia.macros

import scala.reflect.macros.blackbox.{Context => BlackboxContext}
import aptus._

// ===========================================================================
object UndynamizerMacro {

  def apply[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext): bbc.Tree = { import bbc.universe._

    val subTrees: Seq[(ClassName, bbc.Tree)] =
      ClassTraversal
        .parse[$TargetType](bbc.universe)
        .topologicalTraversal
        .map { classUndynamizer(_)(bbc) }

  	// ---------------------------------------------------------------------------
    val methodTrees: Seq[bbc.Tree] =
      subTrees
        .map { case (className, subTree) =>
          val method = TermName(formatVariableName(className))                       
                                 
          q"""def ${method}: Function[${typeOf[gallia.Obj]}, ${TypeName(className)}] = ${subTree}"""}

  	// ---------------------------------------------------------------------------  
    q"""
      ..${methodTrees}
      
      ${TermName(formatVariableName(subTrees.last._1))}
    """
  }     

  // ===========================================================================
  private def classUndynamizer(c: Cls)(bbc: BlackboxContext): Tuple2[ClassName, bbc.Tree] = { import bbc.universe._    

    val entryTrees: Seq[Tree] =
      c .fields
        .map { field =>
          q"${valueUndynamizer(field)(bbc)}" }

    // ---------------------------------------------------------------------------
    c.forceName ->
      q"""
        new Function[${typeOf[gallia.Obj]}, ${TypeName(c.forceName)}] {
          def apply(${TermName(InstanceVariableName)}: ${typeOf[gallia.Obj]}): ${TypeName(c.forceName)} =
            ${TermName(c.forceName)}(..${entryTrees}) }"""       
  }

  // ===========================================================================
  private def valueUndynamizer(field: Fld)(bbc: BlackboxContext): bbc.Tree = { import bbc.universe._
  		val objectVariable = TermName(InstanceVariableName)
  		  
      field.info.containee match {
  
        // ---------------------------------------------------------------------------    
        case c: Cls =>
          val subDynamizerVariable = TermName(formatVariableName(c.forceName))
  
          field.info.container match {
            case Container._One => q"${TermName(field.skey)} = ${subDynamizerVariable}.apply(${objectVariable}.obj  (${field.key}))"                                     //               T
            case Container._Opt => q"${TermName(field.skey)} =                               ${objectVariable}.obj_ (${field.key}).map(${subDynamizerVariable}.apply)"   //        Option[T]
            case Container._Nes => q"${TermName(field.skey)} =                               ${objectVariable}.objs (${field.key}).map(${subDynamizerVariable}.apply)"   //        Seq   [T]      	
            case Container._Pes => q"${TermName(field.skey)} =                               ${objectVariable}.objs_(${field.key}).map(${subDynamizerVariable}.apply)" } // Option[Seq   [T]]

        // ---------------------------------------------------------------------------
        case BasicType._Enum =>         
          val accessorMethod = TermName(formatAccessorMethodName(field.info.container, BasicType._Enum))      
  
          val enumTypeName = TypeName(field.forceEnumName.splitBy(".").last /* TODO: must be in scope... */)

          q"${TermName(field.skey)} = ${objectVariable}.${accessorMethod}[${enumTypeName}](${field.key})"        
          
        // ---------------------------------------------------------------------------
        case basicType: BasicType =>
          val accessorMethod = TermName(formatAccessorMethodName(field.info.container, basicType))      
  
          q"${TermName(field.skey)} = ${objectVariable}.${accessorMethod}(${field.key})"        
      }
    }
  
    // ---------------------------------------------------------------------------
    private def formatAccessorMethodName(container: Container, basicType: BasicType): String = // see ObjAccessors (id210326140514)
  	  basicType
  	    .fullName
  	    .stripPrefix("Local") // eg LocalDate
  	    .replace("EnumEntry", "enm") // "enum" is reserved in Scala 3
  	    .splitBy(".").last.uncapitalizeFirst  + // TODO: store in BasicType rather?  	    
      (container match {
          case Container._One => ""
          case Container._Opt => "_"
          case Container._Nes => "s"
          case Container._Pes => "s_" })

  // ===========================================================================
  private def formatVariableName(name: ClassName): VariableName = gallia.macros.formatVariableName(name, suffix = "Undynamizer")

}

// ===========================================================================
