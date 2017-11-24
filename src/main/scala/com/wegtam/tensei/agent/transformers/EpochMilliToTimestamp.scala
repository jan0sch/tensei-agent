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

import java.time.{ Instant, ZoneId, ZonedDateTime }

import akka.actor.Props
import com.wegtam.tensei.agent.adt.types.{ IntegerData, TimestampData }
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

import scala.util.Try

object EpochMilliToTimestamp {

  def props: Props = Props(new EpochMilliToTimestamp())

  /**
    * Convert the given numeric timestamp into a real one.
    *
    * @param tz The timezone of the timestamp.
    * @param d  An integer parser data value.
    * @return A timestamp parser data value.
    */
  def convertNumericToTimestamp(tz: ZoneId)(d: IntegerData): TimestampData = {
    val i = Instant.ofEpochMilli(d.v)
    val z = ZonedDateTime.ofInstant(i, tz)
    TimestampData(z.toOffsetDateTime)
  }

}

/**
  * This transformer converts any given numeric timestamp into a proper
  * timestamp value.
  *
  * The given timestamp has to be in milliseconds.
  */
class EpochMilliToTimestamp extends BaseTransformer {
  override def transform: Receive = {
    case msg: StartTransformation =>
      log.debug("Starting NumericToTimestamp transformer.")
      val params = msg.options.params
      val timezone = Try(paramValueO("timezone")(params).map(ZoneId.of).getOrElse(ZoneId.of("UTC"))) match {
        case scala.util.Failure(f) =>
          log.error(f, "Could not parse given timezone!")
          ZoneId.of("UTC")
        case scala.util.Success(z) => z
      }
      val convert: IntegerData => TimestampData =
        EpochMilliToTimestamp.convertNumericToTimestamp(timezone)
      val results = msg.src.map {
        case i: IntegerData => convert(i)
        case d              => d
      }
      log.debug("Finished NumericToTimestamp transformer.")
      sender() ! TransformerResponse(results)
      context.become(receive)
  }
}
