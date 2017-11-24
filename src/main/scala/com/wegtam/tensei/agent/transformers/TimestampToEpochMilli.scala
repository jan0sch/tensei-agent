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
import com.wegtam.tensei.agent.adt.types.{ IntegerData, TimestampData }
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

object TimestampToEpochMilli {

  def props: Props = Props(new TimestampToEpochMilli())

  /**
    * Convert the given Timestamp parser data value into a numeric timestamp.
    *
    * @param d A timestamp parser data value.
    * @return An integer parser data value holding the numeric timestamp.
    */
  def convertTimestampToNumeric(d: TimestampData): IntegerData =
    IntegerData(d.v.toInstant.toEpochMilli)
}

/**
  * This transformer converts any given timestamp value into a numeric
  * timestamp e.g. the number of epoch milliseconds.
  */
class TimestampToEpochMilli extends BaseTransformer {
  override def transform: Receive = {
    case msg: StartTransformation =>
      log.debug("Starting NumericToTimestamp transformer.")
      val results = msg.src.map {
        case t: TimestampData => TimestampToEpochMilli.convertTimestampToNumeric(t)
        case d                => d
      }
      log.debug("Finished NumericToTimestamp transformer.")
      sender() ! TransformerResponse(results)
      context.become(receive)
  }
}
