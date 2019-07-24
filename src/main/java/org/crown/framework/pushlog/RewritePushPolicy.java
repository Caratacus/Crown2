package org.crown.framework.pushlog;

import java.text.DateFormat;
import java.util.Date;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 * 利用rewrite把日志推送到页面
 *
 * @author Caratacus
 */
@Plugin(name = "RewritePushPolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public final class RewritePushPolicy implements RewritePolicy {

    @PluginFactory
    public static RewritePushPolicy factory() {
        return new RewritePushPolicy();
    }

    @Override
    public LogEvent rewrite(LogEvent source) {
        LogMessage loggerMessage = new LogMessage(
                source.getMessage().getFormattedMessage(),
                DateFormat.getDateTimeInstance().format(new Date(source.getTimeMillis())),
                source.getSource().getFileName(),
                source.getSource().getLineNumber(),
                source.getThreadName(),
                source.getLevel().name());
        LoggerQueue.getInstance().push(loggerMessage);
        return source;
    }
}