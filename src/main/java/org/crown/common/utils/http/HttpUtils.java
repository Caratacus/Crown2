package org.crown.common.utils.http;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.crown.common.enums.HTTPMethod;
import org.crown.common.utils.JacksonUtils;
import org.crown.common.utils.StringUtils;
import org.crown.common.utils.file.FileUtils;
import org.crown.framework.exception.Crown2Exception;
import org.springframework.util.StreamUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Http 请求工具类
 *
 * @author Caratacus
 * @data 2016-07-04
 */
@SuppressWarnings("ALL")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public abstract class HttpUtils {

    private static final HttpClientBuilder httpClientBuilder;
    private static final RequestConfig defaultRequestConfig;

    static {
        defaultRequestConfig = RequestConfig.copy(RequestConfig.DEFAULT).setSocketTimeout(50000).setConnectTimeout(50000)
                .setConnectionRequestTimeout(50000).build();

        ConnectionKeepAliveStrategy connectionKeepAliveStrategy = (httpResponse, httpContext) -> {
            // tomcat默认keepAliveTimeout为20s
            return 20 * 1000;
        };
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(20, TimeUnit.SECONDS);
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(200);
        httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(connManager);
        httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler());
        httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy);
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @param param
     * @return String
     */
    public static String sendGet(String url, String param) {
        return sendGet(url + "?" + param, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @return String
     */
    public static String sendGet(String url) {
        return sendGet(url, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpGet请求
     *
     * @param url
     * @param headMap
     * @return
     */
    public static String sendGet(String url, Map<String, String> headMap) {
        return send(HTTPMethod.GET, url, null, null, headMap, Collections.EMPTY_MAP);
    }

    /**
     * 发送请求
     *
     * @param method
     * @param url
     * @param proxyhttpHost 代理服务器
     * @param cookieStore   cookie
     * @param headMap
     * @param paramMap
     * @return String
     */
    public static String send(HTTPMethod method, String url, HttpHost proxyhttpHost, CookieStore cookieStore,
                              Map<String, String> headMap, Map<String, String> paramMap) {

        HttpRequestBase httpRequestBase = null;
        switch (method) {
            case GET:
                httpRequestBase = new HttpGet(url);
                break;
            case POST:
                httpRequestBase = new HttpPost(url);
                break;
            case PUT:
                httpRequestBase = new HttpPut(url);
                break;
            case PATCH:
                httpRequestBase = new HttpPatch(url);
                break;
            case DELETE:
                httpRequestBase = new HttpDelete(url);
                break;
            default:
        }
        setConfigs(httpRequestBase, proxyhttpHost, headMap, paramMap);
        CloseableHttpResponse response = execute(httpRequestBase, cookieStore);
        return getHttpResult(httpRequestBase, response);
    }

    /**
     * 发送HttpPost请求
     *
     * @param url
     * @return String
     */
    public static String sendPost(String url) {
        return sendPost(url, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpPost请求
     *
     * @param url
     * @param paramMap
     * @return String
     */
    public static String sendPost(String url, Map<String, String> paramMap) {
        return sendPost(url, Collections.EMPTY_MAP, paramMap);
    }

    /**
     * 发送HttpPost请求
     *
     * @param url
     * @param headMap
     * @param paramMap
     * @return String
     */
    public static String sendPost(String url, Map<String, String> headMap, Map<String, String> paramMap) {
        return send(HTTPMethod.POST, url, null, null, headMap, paramMap);
    }

    /**
     * 统一设置RequestConfig,请求头
     *
     * @param httpRequestBase
     * @param proxyhttpHost
     * @param headMap
     * @param paramMap
     */
    private static void setConfigs(HttpRequestBase httpRequestBase, HttpHost proxyhttpHost, Map<String, String> headMap,
                                   Map<String, String> paramMap) {
        if (httpRequestBase == null) {
            throw new Crown2Exception(HttpServletResponse.SC_BAD_REQUEST, "HttpRequestBase Is Null");
        }
        // 设置代理服务器
        if (null != proxyhttpHost) {
            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).setProxy(proxyhttpHost).build();
            httpRequestBase.setConfig(requestConfig);
            // 设置默认RequestBase
        } else {
            httpRequestBase.setConfig(defaultRequestConfig);
        }

        // 设置请求头
        if (MapUtils.isNotEmpty(headMap)) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                httpRequestBase.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 设置请求体
        if ((httpRequestBase instanceof HttpEntityEnclosingRequestBase) && MapUtils.isNotEmpty(paramMap)) {
            HttpEntityEnclosingRequestBase httpEntityRequestBase = (HttpEntityEnclosingRequestBase) httpRequestBase;
            List<NameValuePair> formParams = new ArrayList<>();
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                // 给参数赋值
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            httpEntityRequestBase.setEntity(entity);
        }
        log.info("INFO: HttpUtils.setConfigs Params is {}", paramMap.toString());
    }

    /**
     * 统一请求
     *
     * @param httpRequestBase
     * @param cookieStore
     * @return
     */
    private static CloseableHttpResponse execute(HttpRequestBase httpRequestBase, CookieStore cookieStore) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        long start = System.currentTimeMillis();
        try {
            // 设置cookie
            if (cookieStore != null) {
                httpClientBuilder.setDefaultCookieStore(cookieStore);
            }
            httpclient = httpClientBuilder.build();
            response = httpclient.execute(httpRequestBase);
            long runTime = System.currentTimeMillis() - start;
            log.info("Request Path:" + httpRequestBase.getURI() + " StatusCode:" + response.getStatusLine().getStatusCode()
                    + " RunTime:" + runTime + "ms.");
        } catch (Exception e) {
            log.error("Error: Method execute execution error ! Request Path:" + httpRequestBase.getURI(), e);
        }
        return response;
    }

    /**
     * 设置RequestConfig,请求头
     *
     * @param httpRequestBase
     * @param headMap
     */
    private static void setConfigs(HttpRequestBase httpRequestBase, Map<String, String> headMap) {
        setConfigs(httpRequestBase, null, headMap, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpPut请求
     *
     * @param url
     * @return String
     */
    public static String sendPut(String url) {
        return sendPut(url, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpPut请求
     *
     * @param url
     * @param paramMap
     * @return String
     */
    public static String sendPut(String url, Map<String, String> paramMap) {
        return sendPut(url, Collections.EMPTY_MAP, paramMap);
    }

    /**
     * 发送HttpPut请求
     *
     * @param url
     * @param paramMap
     * @return String
     */
    public static String sendPut(String url, Map<String, String> headMap, Map<String, String> paramMap) {
        return send(HTTPMethod.PUT, url, null, null, headMap, paramMap);

    }

    /**
     * 发送HttpPatch请求
     *
     * @param url
     * @return String
     */
    public static String sendPatch(String url) {
        return sendPatch(url, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpPatch请求
     *
     * @param url
     * @param paramMap
     * @return String
     */
    public static String sendPatch(String url, Map<String, String> paramMap) {
        return sendPatch(url, Collections.EMPTY_MAP, paramMap);
    }

    /**
     * 发送HttpPatch请求
     *
     * @param url
     * @param paramMap
     * @return String
     */
    public static String sendPatch(String url, Map<String, String> headMap, Map<String, String> paramMap) {
        return send(HTTPMethod.PATCH, url, null, null, headMap, paramMap);

    }

    /**
     * 发送带请求体请求只适用于继承HttpEntityEnclosingRequestBase的请求(POST,PUT,PATCH)
     *
     * @param method
     * @param url
     * @param bodyType
     * @param headMap
     * @param object
     * @param cookieStore
     * @return
     */
    public static String sendEntity(HTTPMethod method, String url, ContentType bodyType, Map<String, String> headMap, Object object,
                                    CookieStore cookieStore) {
        if (StringUtils.isBlank(url) || null == object) {
            throw new Crown2Exception(HttpServletResponse.SC_BAD_REQUEST, "请求路径不能为空");
        }
        HttpEntityEnclosingRequestBase httpEntityMethod = null;
        switch (method) {
            case POST:
                httpEntityMethod = new HttpPost(url);
                break;
            case PUT:
                httpEntityMethod = new HttpPut(url);
                break;
            case PATCH:
                httpEntityMethod = new HttpPatch(url);
                break;
            default:
        }
        setConfigs(httpEntityMethod, null, headMap, Collections.EMPTY_MAP);
        StringEntity stringEntity;
        String jsonEntity = JacksonUtils.toJson(object);
        if (null != bodyType) {
            stringEntity = new StringEntity(jsonEntity, bodyType);
        } else {
            stringEntity = new StringEntity(jsonEntity, Consts.UTF_8);
        }
        httpEntityMethod.setEntity(stringEntity);
        CloseableHttpResponse response = execute(httpEntityMethod, cookieStore);
        return getHttpResult(httpEntityMethod, response);

    }

    /**
     * Post请求
     *
     * @param url
     * @param headMap
     * @param object
     * @return
     * @author Caratacus
     */
    public static String sendPostEntity(String url, Map<String, String> headMap, Object object) {
        return sendEntity(HTTPMethod.POST, url, null, headMap, object, null);

    }

    /**
     * Put请求
     *
     * @param url
     * @param headMap
     * @param object
     * @return
     * @author Caratacus
     */
    public static String sendPutEntity(String url, Map<String, String> headMap, Object object) {
        return sendEntity(HTTPMethod.PUT, url, null, headMap, object, null);

    }

    /**
     * Patch请求
     *
     * @param url
     * @param headMap
     * @param object
     * @return
     * @author Caratacus
     */
    public static String sendPatchEntity(String url, Map<String, String> headMap, Object object) {
        return sendEntity(HTTPMethod.PATCH, url, null, headMap, object, null);

    }

    /**
     * Post请求
     *
     * @param url
     * @param object
     * @return
     * @author Caratacus
     */
    public static String sendPostEntity(String url, Object object) {
        return sendPostEntity(url, Collections.EMPTY_MAP, object);

    }

    /**
     * Put请求
     *
     * @param url
     * @param object
     * @return
     * @author Caratacus
     */
    public static String sendPutEntity(String url, Object object) {
        return sendPutEntity(url, Collections.EMPTY_MAP, object);

    }

    /**
     * Patch请求
     *
     * @param url
     * @param object
     * @return
     * @author Caratacus
     */
    public static String sendPatchEntity(String url, Object object) {
        return sendPatchEntity(url, Collections.EMPTY_MAP, object);

    }

    /**
     * 发送HttpDelete请求
     *
     * @param url
     * @return
     * @author Caratacus
     */
    public static String sendDelete(String url, Map<String, String> headMap) {
        return send(HTTPMethod.DELETE, url, null, null, headMap, Collections.EMPTY_MAP);
    }

    /**
     * 获取http返回
     *
     * @param response
     * @return
     * @author Caratacus
     */
    private static String getHttpResult(HttpRequestBase httpRequestBase, CloseableHttpResponse response) {
        String result = "";
        if (response == null) {
            return result;
        }
        try {
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, Consts.UTF_8);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            log.error("Error: Method getHttpResult execution error ! ", e);
        } finally {
            httpRequestBase.releaseConnection();
            IOUtils.closeQuietly(response);
        }
        return removeSpecialChar(result);
    }

    /**
     * 去除特殊字符
     *
     * @param result
     * @return
     */
    private static String removeSpecialChar(String result) {
        if (StringUtils.isNotEmpty(result)) {
            int length = result.length();
            int i = 0;
            StringBuilder builder = new StringBuilder(256);
            while (i < length) {
                char c = result.charAt(i);
                i++;
                if (c == 65279) {
                    continue;
                }
                builder.append(c);
            }
            result = builder.toString();
        }
        return result;
    }

    /**
     * 发送HttpDelete请求
     *
     * @param url
     * @param param
     * @return
     * @author Caratacus
     */
    public static String sendDelete(String url, String param) {
        return sendDelete(url + "?" + param, Collections.EMPTY_MAP);
    }

    /**
     * 发送HttpDelete请求
     *
     * @param url
     * @return
     * @author Caratacus
     */
    public static String sendDelete(String url) {
        return sendDelete(url, Collections.EMPTY_MAP);
    }

    /**
     * 判断请求方式GET
     *
     * @param request
     * @return
     */
    public static boolean isGet(HttpServletRequest request) {
        return HTTPMethod.GET.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式GET
     *
     * @param request
     * @return
     */
    public static boolean isPost(HttpServletRequest request) {
        return HTTPMethod.POST.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式PUT
     *
     * @param request
     * @return
     */
    public static boolean isPut(HttpServletRequest request) {
        return HTTPMethod.PUT.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式DELETE
     *
     * @param request
     * @return
     */
    public static boolean isDelete(HttpServletRequest request) {
        return HTTPMethod.DELETE.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式PATCH
     *
     * @param request
     * @return
     */
    public static boolean isPatch(HttpServletRequest request) {
        return HTTPMethod.PATCH.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式TRACE
     *
     * @param request
     * @return
     */
    public static boolean isTrace(HttpServletRequest request) {
        return HTTPMethod.TRACE.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式HEAD
     *
     * @param request
     * @return
     */
    public static boolean isHead(HttpServletRequest request) {
        return HTTPMethod.HEAD.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * 判断请求方式OPTIONS
     *
     * @param request
     * @return
     */
    public static boolean isOptions(HttpServletRequest request) {
        return HTTPMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod());
    }

    /**
     * http下载文件
     *
     * @param httpUrl
     * @param dir      文件目录
     * @param fileName 文件名称
     * @return
     */
    public static void httpDownload(String httpUrl, String dir, String fileName) {
        HttpRequestBase httpRequestBase = null;
        CloseableHttpResponse response = null;
        try {
            httpRequestBase = new HttpGet(httpUrl);
            setConfigs(httpRequestBase, null, null, null);
            response = execute(httpRequestBase, null);
            int statusCode = response.getStatusLine().getStatusCode();
            // 如果成功
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                byte[] result = EntityUtils.toByteArray(entity);
                BufferedOutputStream bw = null;
                try {
                    FileUtils.makeDir(dir);
                    bw = new BufferedOutputStream(new FileOutputStream(dir + File.separator + fileName));
                    bw.write(result);
                } catch (Exception e) {
                    log.warn("Warn: Exception on httpDownload.  Cause:" + e);
                } finally {
                    IOUtils.closeQuietly(bw);
                }
            }
            // 如果失败
            else {
                log.warn("Warn: Method httpDownload execute Fail. statusCode:" + statusCode);
            }
        } catch (Exception e) {
            log.warn("Warn: Exception on httpDownload.  Cause:" + e);
        } finally {
            httpRequestBase.releaseConnection();
            IOUtils.closeQuietly(response);
        }
    }

    /**
     * 获取项目请求路径
     *
     * @param request
     * @return
     * @author Caratacus
     */
    public static String domain(HttpServletRequest request) {
        return new StringBuilder(32).append(request.getScheme()).append("://").append(request.getServerName()).append(request.getServerPort() == 80 ? "" : ":" + request.getServerPort()).append(request.getContextPath()).toString();
    }

    /**
     * 用于校验http文件状态
     *
     * @param httpUrl
     * @return
     */
    public static int statusCode(String httpUrl) {
        HttpRequestBase httpRequestBase = null;
        CloseableHttpResponse response = null;
        try {
            httpRequestBase = new HttpGet(httpUrl);
            setConfigs(httpRequestBase, null, null, null);
            response = execute(httpRequestBase, null);
            return response.getStatusLine().getStatusCode();
        } finally {
            httpRequestBase.releaseConnection();
            IOUtils.closeQuietly(response);
        }
    }

    /**
     * 获取请求
     *
     * @param request
     * @return
     */
    public static String getRequestBody(HttpServletRequest request) {
        String requestBody = null;
        if (isContainBody(request))
            try {
                StringWriter writer = new StringWriter();
                IOUtils.copy(request.getInputStream(), writer, StandardCharsets.UTF_8.name());
                requestBody = writer.toString();
            } catch (IOException ignored) {
            }
        return requestBody;
    }

    /**
     * 获取请求
     *
     * @param request
     * @return
     */
    public static byte[] getByteBody(HttpServletRequest request) {
        byte[] body = new byte[0];
        try {
            body = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            log.error("Error: Get RequestBody byte[] fail," + e);
        }
        return body;
    }

    /**
     * 是否包含请求体
     *
     * @param request
     * @return
     */
    public static boolean isContainBody(HttpServletRequest request) {
        return isPost(request) || isPut(request) || isPatch(request);
    }

    public static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }

}