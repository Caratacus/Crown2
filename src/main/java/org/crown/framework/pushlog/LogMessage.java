package org.crown.framework.pushlog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 页面控制台日志实体
 *
 * @author Caratacus
 * @link https://cloud.tencent.com/developer/article/1096792
 */
@Setter
@Getter
@AllArgsConstructor
public class LogMessage {

    private String body;
    private String timestamp;
    private String fileName;
    private int lineNumber;
    private String threadName;
    private String level;
}
