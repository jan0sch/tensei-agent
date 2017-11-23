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

package com.wegtam.tensei.agent.transformers.atomic

import akka.actor.Props
import com.wegtam.tensei.agent.adt.types.IntegerData
import com.wegtam.tensei.agent.transformers.atomic.TimestampAdjuster.TimestampAdjusterMode
import com.wegtam.tensei.agent.transformers.BaseTransformer
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

import scala.util.Try

object TimestampAdjuster {
  def props: Props = Props(new TimestampAdjuster())

  /**
    * A sealed trait for the supported modes of the timestamp adjuster.
    */
  sealed trait TimestampAdjusterMode

  object TimestampAdjusterMode {

    /**
      * Multiply the given numeric timestamp value by 1000.
      */
    case object Add extends TimestampAdjusterMode

    /**
      * Divide the given numeric timestamp value through 1000.
      */
    case object Reduce extends TimestampAdjusterMode

    /**
      * Return the timestamp adjuster mode that is represented
      * by the given string.
      *
      * @param s A string containing a timestamp adjuster mode.
      * @return The decoded timestamp adjuster mode.
      */
    def valueOf(s: String): TimestampAdjusterMode = s match {
      case "Add"    => Add
      case "Reduce" => Reduce
    }
  }

  /**
    * Adjust the given numeric timestamp using the provided mode.
    *
    * @param m The mode that determines how the timestamp will be adjusted.
    * @param t A numeric timestamp value (either seconds or milliseconds).
    * @return The adjusted numeric timestamp value.
    */
  def adjustTimestamp(m: TimestampAdjusterMode)(t: Long): Long =
    m match {
      case TimestampAdjusterMode.Add    => t * 1000
      case TimestampAdjusterMode.Reduce => t / 1000
    }

}

/**
  * The TimestampAdjuster receives a list of timestamps and transforms the
  * value of each timestamp. The atomic approach performs the action before the
  * general transformers that are executed during the migration.
  *
  * Available parameters:
  * `perform` : Add or reduce the value. Values: 'add' -> x*1000 (default), 'reduce' -> x:1000
  *
  *
  */
class TimestampAdjuster extends BaseTransformer {
  override def transform: Receive = {
    case msg: StartTransformation =>
      val params = msg.options.params

      val perform = paramValue("perform")(params)
      val mode =
        Try(TimestampAdjusterMode.valueOf(perform)).toOption.getOrElse(TimestampAdjusterMode.Add)

      val results = msg.src.map {
        case IntegerData(v) => IntegerData(TimestampAdjuster.adjustTimestamp(mode)(v))
        case d              => d
      }

      context become receive
      sender() ! TransformerResponse(results)
  }
}
