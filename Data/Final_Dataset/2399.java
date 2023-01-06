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
package schemacrawler.tools.analysis.associations;


import static java.util.Objects.requireNonNull;

import java.util.List;

import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import sf.util.Multimap;

final class ColumnMatchKeysMap
{

  private final Multimap<String, Column> columnsForMatchKey;
  private final Multimap<Column, String> matchKeysForColumn;

  ColumnMatchKeysMap(final List<Table> tables)
  {
    requireNonNull(tables);
    columnsForMatchKey = new Multimap<>();
    matchKeysForColumn = new Multimap<>();

    for (final Table table: tables)
    {
      mapColumnNameMatches(table);
    }
  }

  public boolean containsKey(final Column column)
  {
    return matchKeysForColumn.containsKey(column);
  }

  public boolean containsKey(final String columnKey)
  {
    return columnsForMatchKey.containsKey(columnKey);
  }

  public List<String> get(final Column column)
  {
    return matchKeysForColumn.get(column);
  }

  public List<Column> get(final String matchKey)
  {
    return columnsForMatchKey.get(matchKey);
  }

  @Override
  public String toString()
  {
    return columnsForMatchKey.toString();
  }

  private void mapColumnNameMatches(final Table table)
  {
    for (final Column column: table.getColumns())
    {
      String matchColumnName = column.getName().toLowerCase();
      if (matchColumnName.endsWith("_id"))
      {
        matchColumnName = matchColumnName
          .substring(0, matchColumnName.length() - 3);
      }
      if (matchColumnName.endsWith("id") && !matchColumnName.equals("id"))
      {
        matchColumnName = matchColumnName
          .substring(0, matchColumnName.length() - 2);
      }
      if (!matchColumnName.equals("id"))
      {
        columnsForMatchKey.add(matchColumnName, column);
        matchKeysForColumn.add(column, matchColumnName);
      }
    }
  }

}
