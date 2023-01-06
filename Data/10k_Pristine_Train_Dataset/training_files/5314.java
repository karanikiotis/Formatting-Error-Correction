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
package org.takes.tk;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.takes.Take;

/**
 * Take that headers.
 *
 * <p>This take wraps all responses of another take, adding
 * cookies to them, through {@link org.takes.rs.RsWithCookie}.
 *
 * <p>The class is immutable and thread-safe.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id: 492bd7c3f3989abfe31fbe21fc8bc153387f6a2c $
 * @since 0.1
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class TkWithCookie extends TkWrap {

    /**
     * Ctor.
     * @param take Original
     * @param key Cookie name
     * @param value Cookie value
     */
    public TkWithCookie(final Take take, final String key, final String value) {
        super(
            new TkWithHeaders(
                take,
                String.format("Set-Cookie: %s=%s", key, value)
        )
        );
    }

}
