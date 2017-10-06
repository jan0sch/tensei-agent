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

package com.wegtam.tensei.agent.adt.types

import java.math.BigDecimal
import java.time.{ LocalDate, LocalTime, OffsetDateTime }

import akka.util.ByteString
import org.dfasdl.utils.types._

/**
  * Provide implicits for several usecases (like typeclasses).
  */
object implicits {

  /**
    * Provide wrapper operations for data types to be wrapped into the appropriate
    * parser data type.
    *
    * @tparam T The data type.
    */
  trait WrapperOps[T] {

    /**
      * Construct a parser data type wrapper from the given input.
      *
      * This function will throw an exception if the given input could not
      * be wrapped!
      *
      * @param t An arbitrary input data type.
      * @return An appropriate parser data type wrapper.
      */
    def wrap(t: T): ParserData

  }

  /**
    * Wrap big decimals.
    */
  implicit object BigDecimalWrapper extends WrapperOps[BigDecimal] {
    override def wrap(t: BigDecimal): ParserData = DecimalData(t)
  }

  /**
    * Wrap byte arrays.
    */
  implicit object ByteArrayWrapper extends WrapperOps[Array[Byte]] {
    override def wrap(t: Array[Byte]): ParserData = BinaryData(t)
  }

  /**
    * Wrap bytestrings.
    */
  implicit object ByteStringWrapper extends WrapperOps[ByteString] {
    override def wrap(t: ByteString): ParserData = StringData(t)
  }

  /**
    * Wrap local dates.
    */
  implicit object LocalDateWrapper extends WrapperOps[LocalDate] {
    override def wrap(t: LocalDate): ParserData = DateData(t)
  }

  /**
    * Wrap local times.
    */
  implicit object LocalTimeWrapper extends WrapperOps[LocalTime] {
    override def wrap(t: LocalTime): ParserData = TimeData(t)
  }

  /**
    * Wrap long numbers.
    */
  implicit object LongWrapper extends WrapperOps[Long] {
    override def wrap(t: Long): ParserData = IntegerData(t)
  }

  /**
    * Wrap offset datetime.
    */
  implicit object OffsetDateTimeWrapper extends WrapperOps[OffsetDateTime] {
    override def wrap(t: OffsetDateTime): ParserData = TimestampData(t)
  }

  /**
    * Wrap strings.
    */
  implicit object StringWrapper extends WrapperOps[String] {
    override def wrap(t: String): ParserData = StringData(ByteString(t))
  }

  /**
    * Provide operations on DFASDL [[org.dfasdl.utils.types.DataElement]] types.
    *
    * @tparam T The dfasdl data element type.
    */
  trait DfasdlDataElementOps[T <: DataElement] {

    /**
      * Convert the given DFASDL data element into a [[com.wegtam.tensei.agent.adt.types.ParserData]] element.
      *
      * @param t A DFASDL data element.
      * @return The appropriate parser data element.
      */
    def toParserData(t: T): ParserData
  }

  /**
    * Operations on [[org.dfasdl.utils.types.BinaryE]] elements.
    */
  implicit object BinaryElementOps extends DfasdlDataElementOps[BinaryE] {
    override def toParserData(t: BinaryE): ParserData = BinaryData(t.v)
  }

  /**
    * Operations on [[org.dfasdl.utils.types.DecimalE]] elements.
    */
  implicit object DecimalElementOps extends DfasdlDataElementOps[DecimalE] {
    override def toParserData(t: DecimalE): ParserData = DecimalData(t.v)
  }

  /**
    * Operations on [[org.dfasdl.utils.types.IntegerE]] elements.
    */
  implicit object IntegerElementOps extends DfasdlDataElementOps[IntegerE] {
    override def toParserData(t: IntegerE): ParserData = IntegerData(t.v)
  }

  /**
    * Operations on [[org.dfasdl.utils.types.LocalDateE]] elements.
    */
  implicit object LocalDateElementOps extends DfasdlDataElementOps[LocalDateE] {
    override def toParserData(t: LocalDateE): ParserData = DateData(t.v)
  }

  /**
    * Operations on [[org.dfasdl.utils.types.LocalTimeE]] elements.
    */
  implicit object LocalTimeElementOps extends DfasdlDataElementOps[LocalTimeE] {
    override def toParserData(t: LocalTimeE): ParserData = TimeData(t.v)
  }

  /**
    * Operations on [[org.dfasdl.utils.types.OffsetDateTimeE]] elements.
    */
  implicit object OffsetDateTimeElementOps extends DfasdlDataElementOps[OffsetDateTimeE] {
    override def toParserData(t: OffsetDateTimeE): ParserData = TimestampData(t.v)
  }

  /**
    * Operations on [[org.dfasdl.utils.types.StringE]] elements.
    */
  implicit object StringElementOps extends DfasdlDataElementOps[StringE] {
    override def toParserData(t: StringE): ParserData = StringData(ByteString(t.v))
  }

}
