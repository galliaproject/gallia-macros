package gallia.macros

import scala.reflect.macros.blackbox.{Context => BlackboxContext}

// ===========================================================================
object SchematizingMacro {
    
  def apply[$TargetType : bbc.universe.WeakTypeTag](bbc: BlackboxContext): bbc.Tree = { import bbc.universe._
    val c = ClassTraversal.parse[$TargetType](bbc.universe).values.head // by design

    // ---------------------------------------------------------------------------
   val lifting: Liftable[Cls] =
      new SchemaLifting(bbc.universe)
        ._cls
        .asInstanceOf[Liftable[Cls]] // not sure why need to recast       
  
    // ---------------------------------------------------------------------------
    lifting(c)
  }
  
  // ===========================================================================
  private class SchemaLifting(val universe: scala.reflect.api.Universe) {  
    import universe._
  
    // ---------------------------------------------------------------------------
    implicit lazy val _enumValue     = Liftable[    EnumValue]  { x => q"${typeOf[EnumValue].typeSymbol.companion}.apply(${x.stringValue})" }
    implicit lazy val _enumValues    = Liftable[Seq[EnumValue]] { x => q"Seq(..$x)" }

    implicit lazy val _basicType = Liftable[BasicType] {
      case e: _Enm => q"${typeOf[_Enm]     .typeSymbol.companion}.apply(${e.values})"
      case x       => q"${typeOf[BasicType].typeSymbol.companion}.withName(${x.entryName})" }

    implicit lazy val _ValueType = Liftable[ValueType] {
      case c: Cls       => _cls.apply(c)
      case b: BasicType => _basicType(b) }

    implicit lazy val _container = Liftable[Container]    { x => q"${typeOf[Container].typeSymbol.companion}.withName(${x.entryName})" }
    
    implicit lazy val _subInfo   = Liftable[SubInfo     ] { x => q"${typeOf[SubInfo]     .typeSymbol.companion}.apply   (${x.multiple}, ${x.valueType})" }
    implicit lazy val _subInfos  = Liftable[Seq[SubInfo]] { x => q"Seq(..$x)" }
    
    implicit lazy val _info      = Liftable[Info     ]    { x => q"${typeOf[Info]     .typeSymbol.companion}.apply   (${x.optional}, ${x.union})" }
    
    implicit lazy val _fld                = Liftable[Fld]      { x => q"${typeOf[Fld]      .typeSymbol.companion}.apply(${x.key}, ${x.info})" }
    implicit lazy val _fldSeq             = Liftable[Seq[Fld]] { x => q"Seq(..$x)" }

    implicit lazy val _cls: Liftable[Cls] = Liftable[Cls]      { x => q"${typeOf[Cls]      .typeSymbol.companion}.apply(${x.fields}).setName(${x.forceName})" }
    implicit lazy val _clsSeq             = Liftable[Seq[Cls]] { x => q"Seq(..$x)" }
    
    implicit lazy val classTraversal = Liftable[ClassTraversal] { x => q"${typeOf[ClassTraversal].typeSymbol.companion}.apply(${x.values})" }  
  }

}  
// ===========================================================================
