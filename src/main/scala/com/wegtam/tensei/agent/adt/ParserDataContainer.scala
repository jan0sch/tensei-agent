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

package com.wegtam.tensei.agent.adt

import java.math.BigDecimal
import java.time.{ LocalDate, LocalTime, OffsetDateTime }

import akka.util.ByteString
import com.wegtam.tensei.agent.adt.types._

/**
  * A container class for parsed data.
  *
  * @param data                The actual data.
  * @param elementId           The ID of the DFASDL element that describes the data.
  * @param dfasdlId            An option to the ID of the DFASDL which defaults to `None`.
  * @param sequenceRowCounter  If the element is the child of a sequence the sequence row counter is stored here.
  * @param dataElementHash     An option to a possibly calculated hash that is used to pinpoint locations of stacked sequence and choice elements.
  */
final case class ParserDataContainer(
    data: ParserData,
    elementId: String,
    dfasdlId: Option[String] = None,
    sequenceRowCounter: Long = -1L,
    dataElementHash: Option[Long] = None
)

object ParserDataContainer {

  /**
    * This is a helper function intended to create a parser data container
    * from arbitrary data.
    *
    * @param a                  An arbitrary data type.
    * @param elementId          The ID of the DFASDL element that describes the data.
    * @param dfasdlId           An option to the ID of the DFASDL which defaults to `None`.
    * @param sequenceRowCounter If the element is the child of a sequence the sequence row counter is stored here.
    * @param dataElementHash    An option to a possibly calculated hash that is used to pinpoint locations of stacked sequence and choice elements.
    * @return
    */
  @deprecated("Please do not use this function. Create the ParserDataContainer properly instead!",
              "1.13.3")
  @throws[MatchError](
    "The provided input data could not be matched to an appropriate ParserData type!"
  )
  def createFromAny(
      a: Any,
      elementId: String,
      dfasdlId: Option[String],
      sequenceRowCounter: Long,
      dataElementHash: Option[Long]
  ): ParserDataContainer = {
    val data: ParserData = a match {
      case ar: Array[Byte]    => BinaryData(ar)
      case ld: LocalDate      => DateData(ld)
      case bd: BigDecimal     => DecimalData(bd)
      case nu: Long           => IntegerData(nu)
      case bs: ByteString     => StringData(bs)
      case st: String         => StringData(ByteString(st))
      case lt: LocalTime      => TimeData(lt)
      case ot: OffsetDateTime => TimestampData(ot)
    }
    ParserDataContainer(
      data = data,
      elementId = elementId,
      dfasdlId = dfasdlId,
      sequenceRowCounter = sequenceRowCounter,
      dataElementHash = dataElementHash
    )
  }

}
