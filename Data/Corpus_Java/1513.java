/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.takes.rs.xe;

import com.jcabi.matchers.XhtmlMatchers;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.takes.rs.RsXslt;

/**
 * Test case for {@link XeSla}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id: 756cb62c879459743e384f998766a822bf344203 $
 * @since 0.3
 */
public final class XeSlaTest {

    /**
     * XeSLA can build XML response.
     * @throws IOException If some problem inside
     */
    @Test
    public void buildsXmlResponse() throws IOException {
        MatcherAssert.assertThat(
            IOUtils.toString(
                new RsXembly(
                    new XeAppend(
                        "root",
                        new XeSla()
                    )
                ).body()
            ),
            XhtmlMatchers.hasXPaths(
                "/root[@sla]"
            )
        );
    }

    /**
     * XeSLA can build HTML response with default XSL template.
     * @throws IOException If some problem inside
     */
    @Test
    public void buildsHtmlResponse() throws IOException {
        MatcherAssert.assertThat(
            IOUtils.toString(
                new RsXslt(
                    new RsXembly(
                        new XeStylesheet("/org/takes/rs/xe/test_sla.xsl"),
                        new XeAppend(
                            "page",
                            new XeSla()
                        )
                    )
                ).body()
            ),
            XhtmlMatchers.hasXPaths(
                "/xhtml:html/xhtml:span"
            )
        );
    }

}
