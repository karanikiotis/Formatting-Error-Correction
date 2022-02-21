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
package schemacrawler.filter;


import java.util.function.Predicate;

import schemacrawler.schema.Routine;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;

public final class FilterFactory
{

  public static Predicate<Routine> routineFilter(final SchemaCrawlerOptions options)
  {
    final Predicate<Routine> routineFilter = new RoutineTypesFilter(options)
      .and(new DatabaseObjectFilter<Routine>(options,
                                             options.getRoutineInclusionRule()))
      .and(new RoutineGrepFilter(options));

    return routineFilter;
  }

  public static Predicate<Table> tableFilter(final SchemaCrawlerOptions options)
  {
    final Predicate<Table> tableFilter = new TableTypesFilter(options)
      .and(new DatabaseObjectFilter<Table>(options,
                                           options.getTableInclusionRule()))
      .and(new TableGrepFilter(options));

    return tableFilter;
  }

  private FilterFactory()
  {
  }

}
