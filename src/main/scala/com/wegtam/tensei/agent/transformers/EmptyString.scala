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
import com.wegtam.tensei.agent.adt.types.StringData
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

import scala.collection.immutable.Seq

object EmptyString {
  def props: Props = Props(new EmptyString())
}

/**
  * This transformer discards the input and returns a single empty string.
  */
class EmptyString extends BaseTransformer {
  override def transform: Receive = {
    case msg: StartTransformation =>
      log.debug("Return an empty string for {} sources.", msg.src.size)
      context become receive
      sender() ! TransformerResponse(Seq(StringData(ByteString.empty)))
  }
}
