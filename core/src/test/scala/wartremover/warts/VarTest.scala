package org.brianmckenna.wartremover
package test

import org.scalatest.FunSuite

import org.brianmckenna.wartremover.warts.Var

class VarTest extends FunSuite {
  test("can't use `var`") {
    val result = WartTestTraverser(Var) {
      var x = 10
      x
    }
    expectResult(List("var is disabled"), "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
  }
  test("can't use `var` in class parameter") {
    val result = WartTestTraverser(Var) {
      class Bar(var x: Int)
    }
    expectResult(List("var is disabled"), "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
  }
  test("can use var with unchecked") {
    val result = WartTestTraverser(Var) {
      @uncheckedWart var x = 10
      x
    }
    expectResult(List.empty, "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
  }
  test("can use var with unchecked on class parameter") {
    println("blah")
    val result = WartTestTraverser(Var) {
      class Foo(@uncheckedWart var x: Int)
    }
    expectResult(List.empty, "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
  }
}
