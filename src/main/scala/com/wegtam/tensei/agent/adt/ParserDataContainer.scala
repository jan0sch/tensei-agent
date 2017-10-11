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

import com.wegtam.tensei.agent.adt.types._

import scala.collection.immutable.Seq

/**
  * A container class for parsed data.
  *
  * @param data               A list with the actual data.
  * @param elementId          The ID of the DFASDL element that describes the data.
  * @param dfasdlId           An option to the ID of the DFASDL which defaults to `None`.
  * @param sequenceRowCounter If the element is the child of a sequence the sequence row counter is stored here.
  * @param dataElementHash    An option to a possibly calculated hash that is used to pinpoint locations of stacked sequence and choice elements.
  */
final case class ParserDataContainer(
    data: Seq[ParserData],
    elementId: String,
    dfasdlId: Option[String],
    sequenceRowCounter: Long,
    dataElementHash: Option[Long]
)

object ParserDataContainer {
  final val DEFAULT_DATA_ELEMENT_HASH: Option[Long] = None
  final val DEFAULT_DFASDL_ID: Option[String]       = None
  final val DEFAULT_SEQUENCE_ROW_COUNTER: Long      = -1L

  /**
    * Create parser data container.
    *
    * @param dataElementHash     An option to a possibly calculated hash that is used to pinpoint locations of stacked sequence and choice elements.
    * @param sequenceRowCounter  If the element is the child of a sequence the sequence row counter is stored here.
    * @param dfasdlId            An option to the ID of the DFASDL which defaults to `None`.
    * @param elementId           The ID of the DFASDL element that describes the data.
    * @param data                The actual data.
    * @return A parser data container.
    */
  def create(dataElementHash: Option[Long])(
      sequenceRowCounter: Long
  )(dfasdlId: Option[String])(elementId: String)(data: ParserData*): ParserDataContainer =
    ParserDataContainer(
      data = data.to[Seq],
      elementId = elementId,
      dfasdlId = dfasdlId,
      sequenceRowCounter = sequenceRowCounter,
      dataElementHash = dataElementHash
    )

  /**
    * Create an empty parser data container which holds no data.
    *
    * @param dataElementHash    An option to a possibly calculated hash that is used to pinpoint locations of stacked sequence and choice elements.
    * @param sequenceRowCounter If the element is the child of a sequence the sequence row counter is stored here.
    * @param dfasdlId           An option to the ID of the DFASDL which defaults to `None`.
    * @param elementId          The ID of the DFASDL element that describes the data.
    * @return An empty parser data container.
    */
  def createEmpty(dataElementHash: Option[Long])(
      sequenceRowCounter: Long
  )(dfasdlId: Option[String])(elementId: String): ParserDataContainer =
    ParserDataContainer(
      data = Seq.empty,
      elementId = elementId,
      dfasdlId = dfasdlId,
      sequenceRowCounter = sequenceRowCounter,
      dataElementHash = dataElementHash
    )

  /**
    * Create an empty parser data container using defaults for the unspecified fields which holds no data.
    *
    * @param elementId The ID of the DFASDL element that describes the data.
    * @return An empty parser data container using default values for unspecified fields.
    */
  def createEmptyWithDefaults(elementId: String): ParserDataContainer =
    createEmpty(DEFAULT_DATA_ELEMENT_HASH)(DEFAULT_SEQUENCE_ROW_COUNTER)(DEFAULT_DFASDL_ID)(
      elementId
    )

  /**
    * Create a parser data container using defaults for the unspecified fields.
    *
    * @param elementId The ID of the DFASDL element that describes the data.
    * @param data      The actual data.
    * @return A parser data container using default values for unspecified fields.
    */
  def createWithDefaults(elementId: String)(data: ParserData*): ParserDataContainer =
    create(DEFAULT_DATA_ELEMENT_HASH)(DEFAULT_SEQUENCE_ROW_COUNTER)(DEFAULT_DFASDL_ID)(elementId)(
      data: _*
    )

}
