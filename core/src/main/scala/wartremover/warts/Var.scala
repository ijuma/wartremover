package org.brianmckenna.wartremover
package warts

object Var extends WartTraverser {
  def apply(u: WartUniverse): u.Traverser = {
    import u.universe._
    val utils = new TraverseUtils[u.type](u)
    import utils.containsUncheckedWart

    val HashCodeName: TermName = "hashCode"

    val allowedTypes = List(
      "scala.xml.MetaData", "scala.xml.NamespaceBinding", // XML literals output vars
      "scala.tools.nsc.interpreter.IMain" // REPL needs this
    ) // cannot do `map rootMirror.staticClass` here because then:
      //   scala.ScalaReflectionException: object scala.tools.nsc.interpreter.IMain in compiler mirror not found.

    new u.Traverser {

      override def traverse(tree: Tree) {
        val synthetic = isSynthetic(u)(tree)
        tree match {
          // Ignore case class's synthetic hashCode
          case ClassDef(mods, _, tparams, Template(parents, self, stats)) if mods.hasFlag(Flag.CASE) =>
            mods.annotations foreach { annotation =>
              traverse(annotation)
            }
            tparams foreach { tparam =>
              traverse(tparam)
            }
            parents foreach { parent =>
              traverse(parent)
            }
            traverse(self)
            stats filter {
              case DefDef(_, HashCodeName, _, _, _, _) =>
                false
              case _ =>
                true
            } foreach { stat =>
              traverse(stat)
            }
          // Scala pattern matching outputs synthetic vars
          case ValDef(mods, _, _, _) if mods.hasFlag(Flag.MUTABLE) && synthetic =>

          // Ignore allowed types
          case ValDef(mods, _, tpt, _) if allowedTypes.contains(tpt.tpe.typeSymbol.fullName) =>

          case vd: ValDef if containsUncheckedWart(vd) =>

          case ValDef(mods, _, tpt, _) if mods.hasFlag(Flag.MUTABLE) =>
            u.error(tree.pos, "var is disabled")
            super.traverse(tree)
          case _ =>
            super.traverse(tree)
        }
      }
    }
  }
}
