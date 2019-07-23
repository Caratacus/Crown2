package org.crown.framework.springboot.properties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Generator {

    /**
     * 作者
     */
    private String author;
    /**
     * 默认生成包路径
     */
    private String packagePath;
    /**
     * 自动去除表前缀，默认是true
     */
    private String autoRemovePre;
    /**
     * 表前缀(类名不会包含表前缀)
     */
    private String tablePrefix;
}
