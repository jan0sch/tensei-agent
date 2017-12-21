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
import argonaut._
import Argonaut._
import com.wegtam.tensei.agent.adt.types._
import com.wegtam.tensei.agent.adt.types.wrappers._
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

import scala.collection.immutable.Seq

object ToJson {
  def props: Props = Props(new ToJson())

  /**
    * Convert the given list of parser data values into JSON strings
    * which will be returned as a list of string parser data values.
    *
    * If labels are defined then they will be used and the JSON structure
    * will be an object holding the parser data value in the labeled attribute.
    *
    * @param ls A list of lables which may be empty.
    * @param ps A list of parser data values.
    * @return A list of string parser data values.
    */
  def toJsonString(ls: Seq[String])(ps: Seq[ParserData]): Seq[ParserData] =
    ps.zipAll(ls, StringData(ByteString("")), "").map { t =>
      val (p, l) = t
      val jsonValue: Json = p match {
        case BinaryData(v)    => v.asJson
        case DateData(v)      => v.asJson
        case DecimalData(v)   => v.asJson
        case IntegerData(v)   => v.asJson
        case StringData(v)    => v.asJson
        case TimeData(v)      => v.asJson
        case TimestampData(v) => v.asJson
      }
      if (l.nonEmpty)
        StringData(ByteString(jsonValue.nospaces.wrap))
      else
        StringData(ByteString(Json(l := jsonValue).nospaces.wrap))
    }
}

/**
  * A transformer that converts the given data into a json string.
  *
  * It accepts an option named `label` to label to json value.
  */
class ToJson extends BaseTransformer with JsonHelpers {
  override def transform: Receive = {
    case StartTransformation(src, options) =>
      log.debug("Starting conversion to json.")
      val label = paramValue("label")(options.params)

      val result = ToJson.toJsonString(Seq(label))(src)

      context.become(receive)
      sender() ! TransformerResponse(result)
  }
}
