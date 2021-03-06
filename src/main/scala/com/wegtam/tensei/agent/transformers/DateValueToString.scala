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
import java.time.{ LocalDate, LocalTime, OffsetDateTime }

import akka.actor.Props
import akka.util.ByteString
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

object DateValueToString {
  def props: Props = Props(new DateValueToString())
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

      val result: List[ByteString] =
        if (format.isEmpty)
          msg.src.map(e => ByteString(e.toString))
        else {
          val formatter = new SimpleDateFormat(format)
          msg.src.map {
            case d: LocalDate          => ByteString(formatter.format(d))
            case d: LocalTime          => ByteString(formatter.format(d))
            case d: OffsetDateTime     => ByteString(formatter.format(d))
            case d: java.sql.Date      => ByteString(formatter.format(d))
            case d: java.sql.Time      => ByteString(formatter.format(d))
            case d: java.sql.Timestamp => ByteString(formatter.format(d))
            case anyType =>
              ByteString(anyType.toString)
          }
        }

      log.debug("DateValueToString transformed '{}' into '{}'", msg.src, result)
      context become receive
      sender() ! TransformerResponse(result, classOf[String])
  }
}
