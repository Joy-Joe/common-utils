package cn.sy.net;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Component
public class HttpUtils {
    private static final MediaType JSON = MediaType.parse("application/json;charset=UTF-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
    private static final MediaType FORMURLENCODED = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");

    @Resource
    private OkHttpClient client;

    /**
     * get 请求
     *
     * @param url 请求url地址
     * @return string
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * get 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     *
     * @param url       请求url地址
     * @param params    请求参数 map
     * @param headerMap 请求头map
     * @return string
     */
    public String doGet(String url, Map<String, String> params, Map<String, String> headerMap) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }
        Request.Builder builder = new Request.Builder();
        if (MapUtils.isNotEmpty(headerMap)) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder.url(sb.toString()).build();
        return execute(request);
    }

    public String doGetWithHeader(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.url(url).build();
        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();

        return execute(request);
    }

    /**
     * post 请求
     *
     * @param url    请求url地址
     * @param params 请求参数 map
     * @return string
     */
    public String doPostFormHeaders(String url, Map<String, String> params, Map<String, String> headerMap) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Headers.Builder headers = new Headers.Builder();

        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder().headers(headers.build()).url(url).post(builder.build())
                .build();
        return execute(request);
    }

    public String doPostForm(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }

        Request request = new Request.Builder().url(url).post(builder.build())
                .build();
        return execute(request);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据, json 字符串
     * @return string
     */
    public String doPostJson(String url, String json) {
        return executePost(url, json, JSON);
    }

    /**
     * post 请求, 请求数据为 json 的字符串
     *
     * @param url  请求url地址
     * @param json 请求数据, json 字符串
     * @return string
     */
    public String doPostJsonWithHeader(String url, String json, Map<String, String> headers) {
        return executeHeaderPost(url, json, headers, JSON);
    }

    public String doPostStr(String url, String str) {
        return executeHeaderPost(url, str, null, FORMURLENCODED);
    }

    public String doPostStrWithHeader(String url, String str, Map<String, String> headers) {
        return executeHeaderPost(url, str, headers, FORMURLENCODED);
    }

    /**
     * post 请求, 请求数据为 xml 的字符串
     *
     * @param url 请求url地址
     * @param xml 请求数据, xml 字符串
     * @return string
     */
    public String doPostXml(String url, String xml) {
        return executePost(url, xml, XML);
    }

    private String executePost(String url, String data, MediaType contentType) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        return execute(request);
    }

    private String executeHeaderPost(String url, String data, Map<String, String> headers, MediaType contentType) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        return execute(request);
    }

    private String execute(Request request) {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.info("error call url:{}", request.url().toString());
            throw new RuntimeException("error " + request.url().toString() + " requestException:", e);
        }
        return "";
    }
}
