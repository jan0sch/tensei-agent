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

/**
  * A sealed trait to wrap the data element types.
  * The general idea is to gain type safety and move away from using the
  * dreaded `Any` as base type.
  */
sealed trait ParserData extends Product with Serializable

/**
  * A wrapper for binary data.
  *
  * @param v An array of bytes.
  */
final case class BinaryData(v: Array[Byte]) extends ParserData

/**
  * A wrapper for date data.
  *
  * @param v A local date.
  */
final case class DateData(v: LocalDate) extends ParserData

/**
  * A wrapper for decimal data.
  *
  * @param v A decimal number.
  */
final case class DecimalData(v: BigDecimal) extends ParserData

/**
  * A wrapper for integer numbers.
  *
  * @param v An integer number (a Long).
  */
final case class IntegerData(v: Long) extends ParserData

/**
  * A wrapper for string data.
  *
  * @param v A bytestring to save memory.
  */
final case class StringData(v: ByteString) extends ParserData

/**
  * A wrapper for time data.
  *
  * @param v A local time.
  */
final case class TimeData(v: LocalTime) extends ParserData

/**
  * A wrapper for timestamp data.
  *
  * @param v An offset datetime.
  */
final case class TimestampData(v: OffsetDateTime) extends ParserData
