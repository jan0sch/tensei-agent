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

import akka.actor.Props
import akka.util.ByteString
import com.wegtam.tensei.agent.adt.types._
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

import scala.collection.immutable.Seq
import scala.util.Try

object ExtractBiggestValue {
  def props: Props = Props(new ExtractBiggestValue())

  /**
    * Extract the "biggest" value from the given list of parser data values.
    * Depending on the input this function generates different output.
    *
    * <ul>
    *   <li>If all input is numeric data then the biggest number is returned
    *   which is either an integer or a decimal.</li>
    *   <li>If different kinds of input values are provided then these are all
    *   converted into strings and the longest string is returned.</li>
    * </ul>
    *
    * @param ds A list of parser data values.
    * @return The "biggest" value which is either a numeric (decimal or integer) or a string value.
    */
  @throws[NoSuchElementException](cause = "The given list is empty.")
  def extractBiggestValue(ds: Seq[ParserData]): ParserData = {
    val ns: Seq[java.math.BigDecimal] = ds.map {
      case DecimalData(v) => v
      case IntegerData(v) => new java.math.BigDecimal(v)
    }
    if (ns.size != ds.size) {
      // We have not only numbers...
      val strs = ds.map {
        case BinaryData(v)    => v.map(b => String.format("%02X", b)).mkString
        case DateData(v)      => v.toString
        case DecimalData(v)   => v.toPlainString
        case IntegerData(v)   => v.toString
        case StringData(v)    => v.utf8String
        case TimeData(v)      => v.toString
        case TimestampData(v) => v.toString
      }
      StringData(ByteString(strs.sortBy(_.length).reverse.head))
    } else {
      val b = ns.sorted.reverse.head
      Try(b.longValueExact()).map(l => IntegerData(l)).getOrElse(DecimalData(b))
    }
  }
}

/**
  * Returns the "maximum" value from the given input.
  * If the given input only contains number then the maximum number is returned.
  * Otherwise the longest string is returned.
  */
class ExtractBiggestValue extends BaseTransformer {
  override def transform: Receive = {
    case msg: StartTransformation =>
      val response = ExtractBiggestValue.extractBiggestValue(msg.src)
      sender() ! TransformerResponse(Seq(response))
      context.become(receive)
  }
}
