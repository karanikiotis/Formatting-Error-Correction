package net.dongliu.requests.json;

import com.alibaba.fastjson.JSON;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author Liu Dong
 */
public class FastJsonProvider implements JsonProvider {
    @Override
    public void marshal(Writer writer, @Nullable Object value) throws IOException {
        JSON.writeJSONString(writer, value);
    }

    @Nullable
    @Override
    public <T> T unmarshal(Reader reader, Type type) throws IOException {
        // will not be used
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public <T> T unmarshal(InputStream inputStream, Charset charset, Type type) throws IOException {
        return JSON.parseObject(inputStream, charset, type);
    }
}
