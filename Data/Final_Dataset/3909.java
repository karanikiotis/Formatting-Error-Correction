/*
 * Copyright 2009-2017. DigitalGlobe, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.mrgeo.hdfs.tile;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.mrgeo.hdfs.partitioners.SplitGenerator;
import org.mrgeo.hdfs.utils.HadoopFileUtils;

import java.io.*;

public class PartitionerSplit extends Splits
{
public static final String SPLIT_FILE = "partitions";
private static final String SPACER = " ";

@Override
public String findSplitFile(Path parent) throws IOException
{
  Path file = new Path(parent, SPLIT_FILE);
  try
  {
    if (HadoopFileUtils.exists(file))
    {
      return file.toString();
    }
  }
  catch (IOException e)
  {
    throw new IOException("Error opening split file: " + file.toString(), e);
  }

  throw new IOException("Split file not found: " + file.toString());
}

@Override
public void generateSplits(SplitGenerator generator)
{
  splits = generator.getPartitions();
}

@Override
public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
{
  int count = in.readInt();
  splits = new PartitionerSplitInfo[count];

  for (int i = 0; i < splits.length; i++)
  {
    splits[i] = new PartitionerSplitInfo(in.readLong(), in.readInt());
  }

}

// Looks like reading and writing Partitioner splits to and from a file was never used
//  public void readSplits(InputStream stream)
//  {
//    Scanner reader = new Scanner(stream);
//    int count = reader.nextInt();
//    splits = new PartitionerSplitInfo[count];
//
//    for (int i = 0; i < splits.length; i++)
//    {
//      splits[i] = new PartitionerSplitInfo(reader.nextLong(), reader.nextInt());
//    }
//
//    reader.close();
//  }
//
//  public void readSplits(Path parent) throws IOException
//  {
//    readSplits(getInputStream(new Path(parent, SPLIT_FILE)));
//  }
//
//  public void writeSplits(OutputStream stream) throws SplitException
//  {
//    if (splits == null)
//    {
//      throw new SplitException("Splits not generated, call readSplits() or generateSplits() first");
//    }
//
//    PrintWriter writer = new PrintWriter(stream);
//    writer.println(splits.length);
//    for (SplitInfo split: splits)
//    {
//      writer.print(split.getTileId());
//      writer.print(SPACER);
//      writer.println(split.getPartition());
//    }
//    writer.close();
//  }
//
//  public void writeSplits(Path parent) throws IOException
//  {
//    writeSplits(getOutputStream(new Path(parent, SPLIT_FILE)));
//  }

protected FileSystem getFileSystem(Path parent) throws IOException
{
  return HadoopFileUtils.getFileSystem(parent);
}

protected boolean fileExists(Path file) throws IOException
{
  return HadoopFileUtils.exists(file);
}

protected InputStream getInputStream(Path path) throws IOException
{
  return getFileSystem(path).open(path);
}

protected OutputStream getOutputStream(Path path) throws IOException
{
  return getFileSystem(path).create(path);
}

public static class PartitionerSplitInfo extends SplitInfo
{
  private int partition;
  private long tileid;

  public PartitionerSplitInfo(long tileid, int partition)
  {
    this.tileid = tileid;
    this.partition = partition;
  }

  public PartitionerSplitInfo()
  {
    partition = -1;
    tileid = -1;
  }

  @Override
  public long getTileId()
  {
    return tileid;
  }

  @Override
  public int getPartition()
  {
    return partition;
  }

  public String toString()
  {
    return "tile id = " + tileid + ", partition = " + partition;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException
  {
    out.writeLong(tileid);
    out.writeInt(partition);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
  {
    tileid = in.readLong();
    partition = in.readInt();
  }

  @Override
  boolean compareEQ(long tileId)
  {
    return tileId == this.tileid;
  }

  @Override
  boolean compareLE(long tileId)
  {
    return tileId <= this.tileid;
  }

  @Override
  boolean compareLT(long tileId)
  {
    return tileId < this.tileid;
  }

  @Override
  boolean compareGE(long tileId)
  {
    return tileId >= this.tileid;
  }

  @Override
  boolean compareGT(long tileId)
  {
    return tileId > this.tileid;
  }
}

}
