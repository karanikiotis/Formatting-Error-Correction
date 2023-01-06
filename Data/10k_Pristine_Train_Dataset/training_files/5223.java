package net.dongliu.requests.body;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static net.dongliu.requests.HttpHeaders.CONTENT_TYPE_BINARY;

/**
 * @author Liu Dong
 */
class BytesRequestBody extends RequestBody<byte[]> {
    BytesRequestBody(byte[] body) {
        super(body, CONTENT_TYPE_BINARY, false);
    }

    @Override public void writeBody(OutputStream os, Charset charset) throws IOException {
        os.write(getBody());
    }
}
