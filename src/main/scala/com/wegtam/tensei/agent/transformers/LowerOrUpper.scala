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

import java.util.Locale

import akka.actor.Props
import akka.util.ByteString
import com.wegtam.tensei.agent.transformers.BaseTransformer.{
  StartTransformation,
  TransformerResponse
}

object LowerOrUpper {
  def props: Props = Props(new LowerOrUpper())

  /**
    * A sealed trait for the modes of the transformer.
    */
  sealed trait LowerOrUpperMode

  object LowerOrUpperMode {

    /**
      * Lower case all characters.
      */
    case object LowerAll extends LowerOrUpperMode

    /**
      * Lower case only the first character.
      */
    case object LowerFirst extends LowerOrUpperMode

    /**
      * Perform no operation which means that the input is returned as is.
      */
    case object NoOp extends LowerOrUpperMode

    /**
      * Upper case all characters.
      */
    case object UpperAll extends LowerOrUpperMode

    /**
      * Upper case only the first character.
      */
    case object UpperFirst extends LowerOrUpperMode

    /**
      * Return the lower or upper mode which is represented by the given string.
      *
      * @param s A string containing a lower or upper mode.
      * @return The de-serialised lower or upper mode.
      */
    @throws[NoSuchElementException](cause = "The given string is not a LowerOrUpperMode!")
    def valueOf(s: String): LowerOrUpperMode = s match {
      case "LowerAll"   => LowerOrUpperMode.LowerAll
      case "LowerFirst" => LowerOrUpperMode.LowerFirst
      case "NoOp"       => LowerOrUpperMode.NoOp
      case "UpperAll"   => LowerOrUpperMode.UpperAll
      case "UpperFirst" => LowerOrUpperMode.UpperFirst
    }

  }

  /**
    * Perform a lower or upper case operation on the given string.
    *
    * @param l The locale that shall be used for the operation.
    * @param m The mode which determines what characters are modified in which way.
    * @param s A string that shall be processed.
    * @return The modified string.
    */
  def perform(l: Locale)(m: LowerOrUpperMode)(s: String): String =
    m match {
      case LowerOrUpperMode.LowerAll   => s.toLowerCase(l)
      case LowerOrUpperMode.UpperAll   => s.toUpperCase(l)
      case LowerOrUpperMode.LowerFirst => s"""${s.take(1).toLowerCase(l)}${s.drop(1)}"""
      case LowerOrUpperMode.UpperFirst => s"""${s.take(1).toUpperCase(l)}${s.drop(1)}"""
    }
}

/**
  * This transformer returns a lower or upper version of the provided string.
  * Available parameter:
  * - `locale` : The locale that shall be used for the operation. Upper and lowercase functions differ depending on the locale! If none is given or an unknown locale string is given then the default locale will be used.
  * - `perform` : Perform the specified transformation. Possible values are:
  *      'lower' : All characters as lower chararcters.
  *      'upper' : All characters as upper characters.
  *      'firstlower: Only the first character as lower character, the other ones are not changed.
  *      'firstupper': Only the first character as upper character, the other ones are not changed.
  */
class LowerOrUpper extends BaseTransformer {
  override def transform: Receive = {
    case msg: StartTransformation =>
      log.debug("Starting LowerOrUpper transformer!")

      val params = msg.options.params

      val locale = paramValueO("locale")(params).fold(Locale.getDefault) { loc =>
        val l = Locale.forLanguageTag(loc)
        if (Locale.getAvailableLocales.contains(l))
          l
        else
          Locale.getDefault
      }
      val mode = LowerOrUpperMode.valueOf(paramValue("perform")(params))

      val results = msg.src.map {
        case StringData(v) =>
          StringData(ByteString(LowerOrUpper.perform(locale)(mode)(v.utf8String)))
        case d => d
      }

      log.debug("Finished lower or upper of src string!")

      context become receive
      sender() ! TransformerResponse(results)
  }
}
