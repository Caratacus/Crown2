package org.crown.project.config;

import org.crown.framework.springboot.properties.CrownProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * Crown项目SpringBoot相关配置
 * </p>
 *
 * @author Caratacus
 * @since 2017-09-21
 */
@Configuration
@EnableConfigurationProperties({CrownProperties.class})
public class CrownAuoConfiguration implements WebMvcConfigurer {

    private final CrownProperties properties;

    public CrownAuoConfiguration(CrownProperties properties) {
        this.properties = properties;
    }

    /**
     * 首页地址
     */
    @Value("${shiro.user.indexUrl}")
    private String indexUrl;

    /**
     * 默认首页的设置，当输入域名是可以自动跳转到默认指定的网页
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:" + indexUrl);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 文件上传路径 */
        registry.addResourceHandler("/profile/**").addResourceLocations("file:" + properties.getPath().getFilePath());

        /** swagger配置 */
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}