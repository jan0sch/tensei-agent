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
import com.wegtam.tensei.agent.adt.types.{ ParserData, StringData }
import com.wegtam.tensei.agent.adt.types.wrappers._
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

import scala.collection.immutable.Seq
import scala.util.Try

object CastStringToLong {
  def props: Props = Props(new CastStringToLong())

  /**
    * Parse the given list of data and cast all strings to long values if possible.
    * Unconvertable strings and non-string values will be omitted from the result list.
    *
    * @param ds A list of data.
    * @return A list of data containing long numbers which may be empty.
    */
  def castStrings(ds: Seq[ParserData]): Seq[ParserData] = {
    val rs = ds.flatMap {
      case StringData(v) => Try(v.utf8String.toLong).toOption
      case _             => None
    }
    rs.map(_.wrap)
  }
}

/**
  * Simply cast the given string to long.
  */
class CastStringToLong extends BaseTransformer {
  override def transform: Receive = {
    case msg: StartTransformation =>
      val rs = CastStringToLong.castStrings(msg.src)
      sender() ! TransformerResponse(rs)
      context.become(receive)
  }
}
