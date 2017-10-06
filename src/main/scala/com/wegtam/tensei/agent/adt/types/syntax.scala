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

import implicits._
import org.dfasdl.utils.types.DataElement

/**
  * Provide syntactic sugar for several operations.
  */
object syntax {

  /**
    * Provid syntactic sugar for DFASDL data element types.
    *
    * @param t A DFASDL data element.
    * @tparam T A DFASDL data element type.
    */
  implicit final class WrapDfasdlDataElementOps[T <: DataElement](val t: T) extends AnyVal {

    /**
      * Convert the DFASDL data element into a [[com.wegtam.tensei.agent.adt.types.ParserData]] element.
      * This provides a `.toParserData` function on a DFASDL
      * [[org.dfasdl.utils.types.DataElement]] if the needed typeclass is in scope.
      *
      * @param ev Implicit evidence for the appropriate operation.
      * @return The converted parser data element.
      */
    def toParserData(implicit ev: DfasdlDataElementOps[T]): ParserData = ev.toParserData(t)

  }

}
