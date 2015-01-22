package org.brianmckenna.wartremover
package test

import org.scalatest.FunSuite

import org.brianmckenna.wartremover.warts.AsInstanceOf

class AsInstanceOfTest extends FunSuite {
  test("asInstanceOf is disabled") {
    val result = WartTestTraverser(AsInstanceOf) {
      "abc".asInstanceOf[String]
    }
    expectResult(List("asInstanceOf is disabled"), "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
  }
  test("asInstanceOf is disabled in a lambda with a synthetic variable") {
    val result = WartTestTraverser(AsInstanceOf) {
      Seq().map(_.asInstanceOf[String])
    }
    expectResult(List("asInstanceOf is disabled"), "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
  }
  test("asInstanceOf is allowed with @uncheckedWart") {
    val result = WartTestTraverser(AsInstanceOf) {
      "abc".asInstanceOf[String]: @uncheckedWart
    }
    expectResult(List.empty, "result.errors")(result.errors)
    expectResult(List.empty, "result.warnings")(result.warnings)
  }
}
