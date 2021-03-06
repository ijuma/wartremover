package org.brianmckenna.wartremover
package test

import org.scalatest.FunSuite

import org.brianmckenna.wartremover.warts.JavaConversions

class JavaConversionsTest extends FunSuite {
   test("handle explicit method reference") {
    val result = WartTestTraverser(JavaConversions) {
      def ff[A](it: Iterable[A]) = collection.JavaConversions.asJavaCollection(it)
      
    }
    expectResult(List("scala.collection.JavaConversions is disabled - use scala.collection.JavaConverters instead"), "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
   
  }  
  test("disable scala.collection.JavaConversions when referenced in an import") {
    val result = WartTestTraverser(JavaConversions) {
      import scala.collection.JavaConversions._
      val x: java.util.List[Int]= new java.util.ArrayList[Int]
      val y: Seq[Int] = x
    }
    expectResult(List("scala.collection.JavaConversions is disabled - use scala.collection.JavaConverters instead"), "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
   
  } 
}
