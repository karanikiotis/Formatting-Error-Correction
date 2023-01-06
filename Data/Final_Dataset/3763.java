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
package schemacrawler.tools.traversal;


import schemacrawler.schema.ColumnDataType;
import schemacrawler.schema.Routine;
import schemacrawler.schema.Sequence;
import schemacrawler.schema.Synonym;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;

public interface SchemaTraversalHandler
  extends TraversalHandler
{

  void handle(final ColumnDataType columnDataType)
    throws SchemaCrawlerException;

  /**
   * Provides information on the database schema.
   *
   * @param routine
   *        Routine metadata.
   */
  void handle(final Routine routine)
    throws SchemaCrawlerException;

  /**
   * Provides information on the database schema.
   *
   * @param sequence
   *        Sequence metadata.
   */
  void handle(final Sequence sequence)
    throws SchemaCrawlerException;

  /**
   * Provides information on the database schema.
   *
   * @param synonym
   *        Synonym metadata.
   */
  void handle(final Synonym synonym)
    throws SchemaCrawlerException;

  /**
   * Provides information on the database schema.
   *
   * @param table
   *        Table metadata.
   */
  void handle(final Table table)
    throws SchemaCrawlerException;

  void handleColumnDataTypesEnd()
    throws SchemaCrawlerException;

  void handleColumnDataTypesStart()
    throws SchemaCrawlerException;

  void handleRoutinesEnd()
    throws SchemaCrawlerException;

  void handleRoutinesStart()
    throws SchemaCrawlerException;

  void handleSequencesEnd()
    throws SchemaCrawlerException;

  void handleSequencesStart()
    throws SchemaCrawlerException;

  void handleSynonymsEnd()
    throws SchemaCrawlerException;

  void handleSynonymsStart()
    throws SchemaCrawlerException;

  void handleTablesEnd()
    throws SchemaCrawlerException;

  void handleTablesStart()
    throws SchemaCrawlerException;

}
