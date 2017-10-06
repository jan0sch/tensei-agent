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

/**
  * Provide syntactic sugar for wrapping arbitrary datatypes into parser data types.
  */
object wrappers {

  /**
    * Provide syntactic sugar for wrappable datatypes.
    *
    * @param t A wrappable data element.
    * @tparam T A wrappable data type.
    */
  implicit final class WrapWrapperOps[T](val t: T) extends AnyVal {

    /**
      * Construct a parser data type wrapper from the given input.
      *
      * This function will throw an exception if the given input could not
      * be wrapped!
      *
      * @param ev Implicit evidence for the appropriate operation.
      * @return An appropriate parser data type wrapper.
      */
    def wrap(implicit ev: WrapperOps[T]): ParserData = ev.wrap(t)

  }

}
