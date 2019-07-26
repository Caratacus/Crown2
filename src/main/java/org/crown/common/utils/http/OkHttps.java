package org.crown.common.utils.http;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.crown.common.enums.HTTPMethod;
import org.crown.common.utils.StringUtils;
import org.crown.framework.exception.Crown2Exception;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * <p>
 * OkHttp工具类
 * </p>
 *
 * @author Caratacus
 */
@Slf4j
public class OkHttps {

    private final static OkHttpClient httpClient;

    private OkHttps() {
    }

    static {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            if (StringUtils.isNotEmpty(message)) {
                log.info(message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().connectTimeout(15L, TimeUnit.SECONDS)
                .readTimeout(10L, TimeUnit.SECONDS)
                .writeTimeout(10L, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    /**
     * 获取httpClient
     *
     * @return
     */
    public static OkHttpClient getClient() {
        return httpClient;
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @return String
     */
    public static String get(String url) {
        return get(url, Collections.emptyMap());
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static String get(String url, Map<String, String> headers) {
        return requestBody(HTTPMethod.GET, url, headers, Collections.emptyMap());
    }

    /**
     * 发送请求
     *
     * @param method
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String requestBody(HTTPMethod method, String url, Map<String, String> headers, Map<String, String> params) {
        Request.Builder requestBuild = new Request.Builder().url(url);
        addHeaders(requestBuild, headers);
        switch (method) {
            case GET:
                requestBuild.get();
            case DELETE:
                requestBuild.delete();
                break;
            case POST:
            case PUT:
            case PATCH:
                FormBody.Builder formBodybuilder = new FormBody.Builder();
                Set<String> paramKeys = params.keySet();
                for (String key : paramKeys) {
                    formBodybuilder.add(key, params.get(key));
                }
                FormBody formBody = formBodybuilder.build();
                requestBuild.method(method.name(), formBody);
                break;
            default:
                throw new Crown2Exception(HttpServletResponse.SC_BAD_REQUEST, "内部请求方式不正确");
        }
        Call call = getClient().newCall(requestBuild.build());
        return getStringResult(call);
    }

    /**
     * 添加请求头
     *
     * @param headers
     * @param requestBuild
     */
    private static void addHeaders(Request.Builder requestBuild, Map<String, String> headers) {
        Set<String> headerKeys = headers.keySet();
        for (String key : headerKeys) {
            requestBuild.addHeader(key, headers.get(key));
        }
    }

    /**
     * 发送HttpPost请求
     *
     * @param url
     * @return String
     */
    public static String post(String url) {
        return post(url, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpPost请求
     *
     * @param url
     * @param params
     * @return String
     */
    public static String post(String url, Map<String, String> params) {
        return post(url, Collections.EMPTY_MAP, params);
    }

    /**
     * 发送HttpPost请求
     *
     * @param url
     * @param headers
     * @param params
     * @return String
     */
    public static String post(String url, Map<String, String> headers, Map<String, String> params) {
        return requestBody(HTTPMethod.POST, url, headers, params);
    }

    /**
     * 发送HttpPut请求
     *
     * @param url
     * @return String
     */
    public static String put(String url) {
        return put(url, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpPut请求
     *
     * @param url
     * @param params
     * @return String
     */
    public static String put(String url, Map<String, String> params) {
        return put(url, Collections.EMPTY_MAP, params);
    }

    /**
     * 发送HttpPut请求
     *
     * @param url
     * @param params
     * @return String
     */
    public static String put(String url, Map<String, String> headers, Map<String, String> params) {
        return requestBody(HTTPMethod.PUT, url, headers, params);

    }

    /**
     * 发送HttpPatch请求
     *
     * @param url
     * @return String
     */
    public static String patch(String url) {
        return patch(url, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpPatch请求
     *
     * @param url
     * @param params
     * @return String
     */
    public static String patch(String url, Map<String, String> params) {
        return patch(url, Collections.EMPTY_MAP, params);
    }

    /**
     * 发送HttpPatch请求
     *
     * @param url
     * @param params
     * @return String
     */
    public static String patch(String url, Map<String, String> headers, Map<String, String> params) {
        return requestBody(HTTPMethod.PATCH, url, headers, params);

    }

    /**
     * 发送带请求体请求只适用于继承HttpEntityEnclosingRequestBase的请求(POST,PUT,PATCH)
     *
     * @param method
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String requestBody(HTTPMethod method, String url, Map<String, String> headers, Object body) {
        Request.Builder requestBuild = new Request.Builder().url(url);
        addHeaders(requestBuild, headers);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(body));
        switch (method) {
            case POST:
                requestBuild.post(requestBody);
                break;
            case PUT:
                requestBuild.put(requestBody);
                break;
            case PATCH:
                requestBuild.patch(requestBody);
                break;
            default:
                throw new Crown2Exception(HttpServletResponse.SC_BAD_REQUEST, "内部请求方式不正确");

        }
        Call call = getClient().newCall(requestBuild.build());
        return getStringResult(call);

    }

    /**
     * Post请求
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String postBody(String url, Map<String, String> headers, Object body) {
        return requestBody(HTTPMethod.POST, url, headers, body);

    }

    /**
     * Put请求
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String putBody(String url, Map<String, String> headers, Object body) {
        return requestBody(HTTPMethod.PUT, url, headers, body);

    }

    /**
     * Patch请求
     *
     * @param url
     * @param headers
     * @param body
     * @return
     */
    public static String patchBody(String url, Map<String, String> headers, Object body) {
        return requestBody(HTTPMethod.PATCH, url, headers, body);

    }

    /**
     * Post请求
     *
     * @param url
     * @param body
     * @return
     */
    public static String postBody(String url, Object body) {
        return postBody(url, Collections.EMPTY_MAP, body);

    }

    /**
     * Put请求
     *
     * @param url
     * @param body
     * @return
     */
    public static String putBody(String url, Object body) {
        return putBody(url, Collections.EMPTY_MAP, body);

    }

    /**
     * Patch请求
     *
     * @param url
     * @param object
     * @return
     */
    public static String patchBody(String url, Object object) {
        return patchBody(url, Collections.EMPTY_MAP, object);

    }

    /**
     * 发送Delete请求
     *
     * @param url
     * @return
     */
    public static String delete(String url, Map<String, String> headers) {
        return requestBody(HTTPMethod.DELETE, url, headers, Collections.EMPTY_MAP);
    }

    /**
     * 发送Delete请求
     *
     * @param url
     * @return
     */
    public static String delete(String url) {
        return delete(url, Collections.EMPTY_MAP);
    }

    /**
     * 获取请求返回
     *
     * @param call
     * @return
     */
    private static String getStringResult(Call call) {
        String result = "";
        try {
            result = call.execute().body().string();
        } catch (Exception ignored) {
        }
        return result;
    }

}
