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

import java.text.SimpleDateFormat

import akka.actor.Props
import akka.util.ByteString
import com.wegtam.tensei.agent.adt.types._
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

import scala.collection.immutable.Seq

object DateValueToString {
  def props: Props = Props(new DateValueToString())

  /**
    * Parse the given list of parser data values and convert each date/time
    * value into a formatted string specified by the given dateformat.
    *
    * @param df The formatter to be used.
    * @param ds A list of parser data values.
    * @return A list of parser data values in which the date/time values are replaced by formatted string values.
    */
  def dateTimeValueToString(df: SimpleDateFormat)(ds: Seq[ParserData]): Seq[ParserData] =
    ds.map {
      case DateData(v)      => StringData(ByteString(df.format(v)))
      case TimeData(v)      => StringData(ByteString(df.format(v)))
      case TimestampData(v) => StringData(ByteString(df.format(v)))
      case d                => d
    }
}

/**
  * Converts a given `Date`, `Time` or `Datetime` value to String. The `format`
  * parameter can be used to define a different target format of the value. If
  * the `format` parameter is empty, the value is simply converted into a String.
  *
  * The transformer accepts the following parameters:
  * - `format`: A target format that is used to transform the given `Date`,
  *             `Time` or `Datetime` value. If this parameter is empty, the
  *             value is simply converted into String.
  */
class DateValueToString extends BaseTransformer {
  override def transform: Receive = {
    case msg: StartTransformation =>
      log.debug("Start DateValueToString")
      val params = msg.options.params
      val format = paramValue("format")(params).trim
      val df     = new SimpleDateFormat(format)
      val rs     = DateValueToString.dateTimeValueToString(df)(msg.src)

      log.debug("DateValueToString transformed '{}' into '{}'", msg.src, rs)
      context become receive
      sender() ! TransformerResponse(rs)
  }
}
