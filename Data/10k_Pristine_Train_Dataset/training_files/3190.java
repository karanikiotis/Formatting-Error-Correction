/*
 * Copyright 2003-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.mps.idea.testFramework;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MpsTestDataParser implements EntryDataParser {
  private final static Logger LOG = LogManager.getLogger(MpsTestDataParser.class);
  @NonNls
  private final static ResourceBundle BUNDLE = ResourceBundle.getBundle("jetbrains.mps.idea.core.MPSCoreBundle");
  private final static String DELIMITERS = "\n\t\f\r;";

  private final FileDataParser myParser;

  public MpsTestDataParser(@NotNull File file) throws FileNotFoundException {
    myParser = new FileDataParser(file, DELIMITERS);
  }

  public void close() {
    myParser.close();
  }

  @Override
  public Entry nextToken(Class<? extends Entry> aClass) throws ParseException {
    try {
      Entry result = aClass.newInstance();
      String token = nextTokenChecked();
      result.parse(new LineDataParser(token));
      return result;
    } catch (InstantiationException e) {
      throw new ParseException(e);
    } catch (IllegalAccessException e) {
      throw new ParseException(e);
    }
  }

  @NotNull
  private String nextTokenChecked() throws ParseException {
    String token = myParser.nextToken(DELIMITERS);
    if (token == null) {
      throw new ParseException("Next token cannot be read.");
    }
    return token;
  }

  @Override
  public boolean hasMoreTokens() {
    return myParser.hasMoreTokens();
  }

  public static interface InnerDataParser {
    @Nullable
    String nextToken(String delimiter);

    int countTokens();

    boolean hasMoreTokens();
  }

  static class LineDataParser implements InnerDataParser {
    private final StringTokenizer myTokenizer;

    private LineDataParser(@NotNull String string) {
      myTokenizer = new StringTokenizer(string);
    }

    public LineDataParser(@NotNull String string, String delimiters) {
      myTokenizer = new StringTokenizer(string, delimiters, false);
    }

    @Nullable
    @Override
    public String nextToken(String delimiters) {
      if (!hasMoreTokens()) {
        return null;
      }

      return myTokenizer.nextToken(delimiters);
    }

    @Override
    public int countTokens() {
      return myTokenizer.countTokens();
    }

    @Override
    public boolean hasMoreTokens() {
      return myTokenizer.hasMoreTokens();
    }
  }

  static class FileDataParser implements InnerDataParser {
    private final Scanner myScanner;
    private final String myDelimiters;
    private LineDataParser myParser = null;

    private FileDataParser(File file, String delimiters) throws FileNotFoundException {
      myScanner = new Scanner(new BufferedReader(new FileReader(file)));
      myDelimiters = delimiters;
    }

    public void close() {
      if (myScanner.hasNext()) {
        LOG.warn(BUNDLE.getString("closing.stream.while.there.are.unread.tokens"));
      }
      myScanner.close();
    }

    private boolean initDataParserIfNeeded() {
      while (myParser == null || !myParser.hasMoreTokens()) {
        if (!myScanner.hasNextLine()) {
          return false;
        }
        String line = myScanner.nextLine();
        line = line.trim();
        myParser = new LineDataParser(line, myDelimiters);
      }
      return true;
    }

    @Nullable
    @Override
    public String nextToken(String delimiters) {
      if (!initDataParserIfNeeded()) {
        return null;
      }

      return myParser.nextToken(delimiters);
    }

    @Override
    public int countTokens() {
      if (!initDataParserIfNeeded()) {
        return 0;
      }

      return myParser.countTokens();
    }

    @Override
    public boolean hasMoreTokens() {
      if (!initDataParserIfNeeded()) {
        return false;
      }

      return myParser.hasMoreTokens();
    }
  }
}
