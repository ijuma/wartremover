package org.brianmckenna.wartremover

class TraverseUtils[U <: WartUniverse](val u: U) {
  import u.universe._

  object UncheckedWartAnnotation {
    def unapply(tpt: TypeTree): Boolean = tpt.tpe match {
      case AnnotatedType(annotations, _) => annotations.exists(_.tree.tpe =:= typeOf[uncheckedWart])
      case _ => false
    }
  }

  def containsUncheckedWart(tree: MemberDef): Boolean = {
    val annots: List[Tree] = tree.symbol.annotations match {
      case Nil => tree.mods.annotations
      case anns => anns.map(_.tree)
    }
    annots.exists(_.tpe =:= typeOf[uncheckedWart])
  }

}
