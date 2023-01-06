/*
   Copyright (c) 2015 LinkedIn Corp.

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

package com.linkedin.multipart.exceptions;


/**
 * Represents an error when trying to re-register a callback with a
 * {@link com.linkedin.multipart.MultiPartMIMEReader.SinglePartMIMEReader}.
 *
 * @author Karim Vidhani
 */
public class SinglePartBindException extends GeneralMultiPartMIMEReaderStreamException
{
  private static final long serialVersionUID = 1L;

  public SinglePartBindException(String message)
  {
    super(message);
  }
}