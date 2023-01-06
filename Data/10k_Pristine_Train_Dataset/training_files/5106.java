package com.code.library.http.builder;

import com.code.library.http.request.GetRequest;
import com.code.library.http.request.RequestCall;
import com.code.library.http.utils.SignatureUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class GetBuilder extends OkHttpRequestBuilder implements HasParamsable {

    private String sign;
    private String key;

    @Override
    public RequestCall build() {
        if (params != null) {
            url = appendParams(url, params);
        }

        //签名
        if (sign == null || sign.equals("")) {
            url = SignatureUtils.signature(SignatureUtils.GET, url, key);
        } else {
            url = url + sign;
        }

        return new GetRequest(url, tag, params, headers).build();
    }

    protected String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public GetBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public GetBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public GetBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public GetBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public GetBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public GetBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }

    public GetBuilder sign(String sign) {
        this.sign = sign;
        return this;
    }

    public GetBuilder key(String key) {
        this.key = key;
        return this;
    }
}
