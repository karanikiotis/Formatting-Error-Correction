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

package schemacrawler.tools.text.operation;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.options.OutputOptions;
import schemacrawler.tools.text.base.BaseJsonFormatter;
import schemacrawler.tools.text.utility.org.json.JSONArray;
import schemacrawler.tools.text.utility.org.json.JSONException;
import schemacrawler.tools.text.utility.org.json.JSONObject;
import schemacrawler.tools.traversal.DataTraversalHandler;
import schemacrawler.utility.Query;

/**
 * Text formatting of data.
 *
 * @author Sualeh Fatehi
 */
final class DataJsonFormatter
  extends BaseJsonFormatter<OperationOptions>
  implements DataTraversalHandler
{

  private final Operation operation;
  private final JSONArray jsonDataArray;

  /**
   * Text formatting of data.
   *
   * @param operation
   *        Options for text formatting of data
   * @param options
   *        Options for text formatting of data
   * @param outputOptions
   *        Options for text formatting of data
   */
  DataJsonFormatter(final Operation operation,
                    final OperationOptions options,
                    final OutputOptions outputOptions)
    throws SchemaCrawlerException
  {
    super(options, /* printVerboseDatabaseInfo */false, outputOptions);
    this.operation = operation;

    jsonDataArray = new JSONArray();
    try
    {
      if (operation != null)
      {
        jsonRoot.put("description", operation.getDescription());
      }
      jsonRoot.put("data", jsonDataArray);
    }
    catch (final JSONException e)
    {
      throw new SchemaCrawlerException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleData(final Query query, final ResultSet rows)
    throws SchemaCrawlerException
  {
    String title;
    if (query != null)
    {
      title = query.getName();
    }
    else
    {
      title = "";
    }

    handleData(title, rows);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleData(final Table table, final ResultSet rows)
    throws SchemaCrawlerException
  {
    final String tableName;
    if (table != null)
    {
      if (options.isShowUnqualifiedNames())
      {
        tableName = table.getName();
      }
      else
      {
        tableName = table.getFullName();
      }
    }
    else
    {
      tableName = "";
    }

    handleData(tableName, rows);
  }

  /**
   * Handles an aggregate operation, such as a count, for a given table.
   *
   * @param title
   *        Title
   * @param results
   *        Results
   */
  private long handleAggregateOperationForTable(final String title,
                                                final ResultSet results)
    throws SchemaCrawlerException
  {
    try
    {
      long aggregate = 0;
      if (results.next())
      {
        aggregate = results.getLong(1);
      }
      return aggregate;
    }
    catch (final SQLException e)
    {
      throw new SchemaCrawlerException("Could not obtain aggregate data", e);
    }
  }

  private void handleData(final String title, final ResultSet rows)
    throws SchemaCrawlerException
  {
    if (rows == null)
    {
      return;
    }

    try
    {
      final JSONObject jsonData = new JSONObject();
      jsonData.put("title", title);

      if (operation == Operation.count)
      {
        final long aggregate = handleAggregateOperationForTable(title, rows);
        jsonData.put("value", aggregate);
      }
      else
      {
        try
        {
          final DataResultSet dataRows = new DataResultSet(rows,
                                                           options
                                                             .isShowLobs());

          jsonData.put("columnNames", new JSONArray(dataRows.getColumnNames()));

          final JSONArray jsonRows = iterateRows(dataRows);
          jsonData.put("rows", jsonRows);
        }
        catch (final SQLException e)
        {
          throw new SchemaCrawlerException(e.getMessage(), e);
        }
      }

      jsonDataArray.put(jsonData);
    }
    catch (final JSONException e)
    {
      throw new SchemaCrawlerException("Could not convert data to JSON", e);
    }

  }

  private JSONArray iterateRows(final DataResultSet dataRows)
    throws SQLException
  {
    final JSONArray jsonRows = new JSONArray();
    while (dataRows.next())
    {
      final List<Object> currentRow = dataRows.row();
      jsonRows.put(new JSONArray(currentRow));
    }
    return jsonRows;
  }
}
