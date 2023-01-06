/*
   Copyright (c) 2012 LinkedIn Corp.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/* $Id$ */
package test.r2.perf.client;

import com.linkedin.r2.message.rest.RestRequest;
import com.linkedin.r2.message.rest.RestRequestBuilder;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import test.r2.perf.Generator;
import test.r2.perf.StringGenerator;


/**
 * @author Chris Pettitt
 * @version $Revision$
 */
public class RestRequestGenerator implements Generator<RestRequest>
{
  private static final String HTTP_POST_METHOD = "POST";
  private static final String STATIC_HEADER_PREFIX = "X-LI-HEADER-";

  private final int _numHeaders;
  private final String _headerContent;
  private final URI _uri;
  private final StringGenerator _generator;
  private final AtomicInteger _msgCounter;

  public RestRequestGenerator(URI uri, int numMsgs, int msgSize, int numHeaders, int headerSize)
  {
    this(uri, numMsgs, numHeaders, headerSize, new StringGenerator(msgSize));
  }

  public RestRequestGenerator(URI uri, int numMsgs, int numHeaders, int headerSize, StringGenerator generator)
  {
    _uri = uri;
    _generator = generator;
    _msgCounter = new AtomicInteger(numMsgs);
    _numHeaders = numHeaders;
    _headerContent = new StringGenerator(headerSize).nextMessage();
  }

  @Override
  public RestRequest nextMessage()
  {
    if (_msgCounter.getAndDecrement() > 0)
    {
      RestRequestBuilder builder = new RestRequestBuilder(_uri);
      builder.setEntity(_generator.nextMessage().getBytes());
      builder.setMethod(HTTP_POST_METHOD);
      for (int i = 0; i < _numHeaders; i++)
      {
        builder.setHeader(STATIC_HEADER_PREFIX + i, _headerContent);
      }
      return builder.build();
    }
    else
    {
      return null;
    }
  }
}
