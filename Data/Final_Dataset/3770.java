/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2017, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/

package schemacrawler.tools.executable;


import java.sql.Connection;

import schemacrawler.schema.Catalog;

/**
 * A SchemaCrawler tools executable unit.
 *
 * @author Sualeh Fatehi
 */
public interface StagedExecutable
{

  /**
   * Executes functionality for SchemaCrawler, after database metadata
   * has been obtained.
   *
   * @param connection
   *        Database connection
   * @throws Exception
   *         On an exception
   */
  void executeOn(Catalog catalog, Connection connection)
    throws Exception;

}
