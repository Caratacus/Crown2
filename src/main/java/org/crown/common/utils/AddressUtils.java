package org.crown.common.utils;

import org.crown.common.utils.http.HttpUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 获取地址类
 *
 * @author ruoyi
 */
public class AddressUtils {

    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    public static String getRealAddressByIP(String ip) {
        String address = "未获取地址";
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        String repoStr = HttpUtils.sendPost(IP_URL,
                Maps.<String, String>builder()
                        .put("accept", "*/*")
                        .put("connection", "Keep-Alive")
                        .put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
                        .put("Accept-Charset", "utf-8")
                        .put("contentType", "utf-8")
                        .build(),
                Maps.<String, String>builder()
                        .put("ip", ip)
                        .build());
        if (StringUtils.isNotEmpty(repoStr)) {
            JSONObject jsonObject = JSONObject.parseObject(repoStr);
            if (jsonObject.getIntValue("code") == 0) {
                JSONObject data = jsonObject.getJSONObject("data");
                String country = data.getString("country");
                String region = data.getString("region");
                String city = data.getString("city");
                address = country + "-" + region + "-" + city;
            }
        }
        return address;
    }
}
