package org.crown.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.crown.common.utils.http.HttpUtils;
import org.crown.framework.spring.ApplicationUtils;
import org.crown.framework.springboot.properties.CrownProperties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * ClassUtils
 *
 * @author Caratacus
 * @date 2017/07/08
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Crowns {

    /**
     * 根据文件名获取下载地址
     *
     * @param filename 文件名
     * @return
     */
    public static String getDownloadPath(String filename) {
        CrownProperties property = getProperties();
        return property.getPath().getFilePath() + property.getPath().getPrefix().getDownload() + filename;
    }

    /**
     * 获取头像上传路径
     *
     * @return
     */
    public static String getAvatarUploadPath() {
        CrownProperties property = getProperties();
        return property.getPath().getFilePath() + property.getPath().getPrefix().getAvatar();
    }

    /**
     * 获取头像上传路径
     *
     * @return
     */
    public static String getUploadPath() {
        CrownProperties property = getProperties();
        return property.getPath().getFilePath() + property.getPath().getPrefix().getUpload();
    }

    /**
     * 获取头像上传路径
     *
     * @return
     */
    public static String getUploadResourcePath(String filename) {
        CrownProperties property = getProperties();
        return property.getPath().getResourcePath() + property.getPath().getPrefix().getUpload() + filename;
    }

    /**
     * 获取头像上传路径
     *
     * @return
     */
    public static String getUploadUrl(HttpServletRequest request, String filename) {
        return HttpUtils.getDomain(request) + getUploadResourcePath(filename);
    }

    /**
     * 获取CrownProperties
     *
     * @return
     */
    public static CrownProperties getProperties() {
        return ApplicationUtils.getBean(CrownProperties.class);
    }

}
