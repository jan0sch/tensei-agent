/*
 * Copyright (C) 2014 - 2017  Contributors as noted in the AUTHORS.md file
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.wegtam.tensei.agent.transformers

import com.wegtam.tensei.agent.ActorSpec
import com.wegtam.tensei.agent.adt.types.wrappers._
import org.scalatest.prop.PropertyChecks

import scala.util.Try

class CastStringToLongTest extends ActorSpec with PropertyChecks {
  describe("CastStringToLong") {
    describe("using valid numbers") {
      it("should return the numbers") {
        forAll() { (a: Long, b: Long, c: Long) =>
          val is = List(a.toString.wrap, b.toString.wrap, c.toString.wrap)
          val es = List(a.wrap, b.wrap, c.wrap)
          CastStringToLong.castStrings(is) should be(es)
        }
      }
    }

    describe("containing invalid input") {
      it("should omit invalid values") {
        forAll() { (a: Long, b: String, c: Long, d: String) =>
          whenever(Try(b.toLong).isFailure && Try(d.toLong).isFailure) {
            val is = List(a.toString.wrap, b.wrap, c.toString.wrap, d.wrap)
            val es = List(a.wrap, c.wrap)
            CastStringToLong.castStrings(is) should be(es)
          }
        }
      }
    }
  }
}
