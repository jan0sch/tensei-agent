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

import com.wegtam.tensei.agent.adt.BaseParserResponseStatus.BaseParserResponseStatusType
import com.wegtam.tensei.agent.adt.types.ParserData
import org.dfasdl.utils.DataElementType.DataElementType

/**
  * A container for a parser response.
  *
  * @param data        An option to the data that was read.
  * @param elementType The specific type of the data element (e.g. StringDataType or BinaryDataElement).
  * @param offset      The last offset in the data stream.
  * @param status      A status message.
  */
final case class BaseParserResponse(
    data: Option[ParserData],
    elementType: DataElementType,
    offset: Long,
    status: BaseParserResponseStatusType
)

object BaseParserResponse {
  final val DEFAULT_OFFSET: Long                         = 0L
  final val DEFAULT_STATUS: BaseParserResponseStatusType = BaseParserResponseStatus.OK

  /**
    * Create a base parser response.
    *
    * @param status      A status message.
    * @param offset      The last offset in the data stream.
    * @param elementType The specific type of the data element (e.g. StringDataType or BinaryDataElement).
    * @param data        The data that was read.
    * @return A base parser response.
    */
  def create(
      status: BaseParserResponseStatusType
  )(offset: Long)(elementType: DataElementType)(data: ParserData): BaseParserResponse =
    BaseParserResponse(
      data = Option(data),
      elementType = elementType,
      offset = offset,
      status = status
    )

  /**
    * Create an empty base parser response which holds no data.
    *
    * @param status      A status message.
    * @param offset      The last offset in the data stream.
    * @param elementType The specific type of the data element (e.g. StringDataType or BinaryDataElement).
    * @return An empty base parser response.
    */
  def createEmpty(
      status: BaseParserResponseStatusType
  )(offset: Long)(elementType: DataElementType): BaseParserResponse =
    BaseParserResponse(
      data = None,
      elementType = elementType,
      offset = offset,
      status = status
    )

  /**
    * Create an empty base parser response which holds no data using defaults for unspecified fields.
    *
    * @param elementType The specific type of the data element (e.g. StringDataType or BinaryDataElement).
    * @return An empty base parser response.
    */
  def createEmptyWithDefaults(elementType: DataElementType): BaseParserResponse =
    createEmpty(DEFAULT_STATUS)(DEFAULT_OFFSET)(elementType)

  /**
    * Create a base parser response using defaults for unspecified fields.
    *
    * @param elementType The specific type of the data element (e.g. StringDataType or BinaryDataElement).
    * @param data        The data that was read.
    * @return A base parser response.
    */
  def createWithDefaults(elementType: DataElementType)(data: ParserData): BaseParserResponse =
    create(DEFAULT_STATUS)(DEFAULT_OFFSET)(elementType)(data)

}
