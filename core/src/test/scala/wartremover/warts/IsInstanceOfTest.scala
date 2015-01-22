package org.brianmckenna.wartremover
package test

import org.scalatest.FunSuite

import org.brianmckenna.wartremover.warts.IsInstanceOf

class IsInstanceOfTest extends FunSuite {
  test("isInstanceOf is disabled") {
    val result = WartTestTraverser(IsInstanceOf) {
      "abc".isInstanceOf[String]
    }
    expectResult(List("isInstanceOf is disabled"), "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
  }
  test("isInstanceOf is disabled in a lambda with a synthetic variable") {
    val result = WartTestTraverser(IsInstanceOf) {
      Seq().filterNot(_.isInstanceOf[String])
    }
    expectResult(List("isInstanceOf is disabled"), "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
  }
}
