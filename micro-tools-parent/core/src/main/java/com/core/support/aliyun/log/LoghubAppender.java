package com.core.support.aliyun.log;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.producer.LogProducer;
import com.aliyun.openservices.log.producer.ProducerConfig;
import com.aliyun.openservices.log.producer.ProjectConfig;
import com.common.support.enums.SystemEn;
import com.core.exception.LogicException;
import com.core.listenters.SpringHolde;
import com.core.support.security.CurrentPrincipalHolder;
import com.core.utils.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class LoghubAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private ProducerConfig config = new ProducerConfig();
    private LogProducer producer;
    private ProjectConfig projectConfig = new ProjectConfig();
    private String logstore;
    private String topic = "";
    private String timeZone = "UTC";
    private String timeFormat = "yyyy-MM-dd'T'HH:mmZ";
    private SimpleDateFormat formatter;
    private ThrowableRenderer throwableRenderer = new ThrowableRenderer();

    public void start() {
        formatter = new SimpleDateFormat(timeFormat);
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        // producer = new LogProducer(config);
        // producer.setProjectConfig(projectConfig);
        super.start();
    }

    public synchronized void initializationProducer() {
        if (SpringHolde.isInitialization() == false)
            throw new LogicException("未初始化上下文");
        org.springframework.core.env.Environment environment = SpringHolde
                .getBean(org.springframework.core.env.Environment.class);
        // 非生产环境不写入日志采集器
        if (!Arrays.asList(environment.getActiveProfiles()).contains("product")) {
            throw new LogicException("环境禁用");
        }
        projectConfig = new ProjectConfig();
        String projectName = environment.getProperty("aliyun.log.projectName");
        if (StringUtils.isBlank(projectName)) {
            throw new LogicException("环境参数错误");
        }
        logstore = environment.getProperty("aliyun.log.logstore");
        String endpoint = environment.getProperty("aliyun.log.endpoint");
        String accessKeyId = environment.getProperty("aliyun.log.accessKeyId");
        String accessKey = environment.getProperty("aliyun.log.accessKey");
        projectConfig = new ProjectConfig(projectName, endpoint, accessKeyId, accessKey);
        producer = new LogProducer(config);
        producer.setProjectConfig(projectConfig);
    }

    @Override
    protected void append(ILoggingEvent event) {
        try {
            if (producer == null) {
                try {
                    initializationProducer();
                } catch (Exception e) {
                    return;
                }
            }
            if ("debug".equalsIgnoreCase(event.getLevel().levelStr)) {
                return;
            }
            List<LogItem> logItems = new ArrayList<LogItem>();
            LogItem item = new LogItem();
            logItems.add(item);
            item.SetTime((int) (event.getTimeStamp() / 1000));
            item.PushBack("time", formatter.format(new Date(event.getTimeStamp())));
            item.PushBack("level", event.getLevel().toString());
            item.PushBack("thread", event.getThreadName());
            item.PushBack("location", event.getLoggerName());
            event.getLoggerName();

            if (CurrentPrincipalHolder.getPrincipal() != null) {
                item.PushBack("system", SystemEn.toEnum(CurrentPrincipalHolder.getPrincipal().getSystemType()).getMean());
                item.PushBack("uid", String.valueOf(CurrentPrincipalHolder.getPrincipal().getUid()));
            }
            if (RequestContextHolder.getRequestAttributes() != null) {// 获取上下文request
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                        .currentRequestAttributes()).getRequest();
                item.PushBack("ip", WebUtils.getRemoteAddr(request));
            }
            StringBuilder sbuf = new StringBuilder(event.getFormattedMessage());
            if (event.getThrowableProxy() != null) {
                throwableRenderer.render(sbuf, event);
            }
            Map properties = event.getMDCPropertyMap();
            if (properties.size() > 0) {
                Object[] keys = properties.keySet().toArray();
                Arrays.sort(keys);
                for (int i = 0; i < keys.length; i++) {
                    item.PushBack(keys[i].toString(), properties.get(keys[i]).toString());
                }
            }

            item.PushBack("message", sbuf.toString());
            producer.send(projectConfig.projectName, logstore, topic, null, logItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
        formatter = new SimpleDateFormat(timeFormat);
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    public void close() {
        producer.flush();
        producer.close();
    }

    public boolean requiresLayout() {
        return false;
    }

    public String getLogstore() {
        return logstore;
    }

    public void setLogstore(String logstore) {
        this.logstore = logstore;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        formatter = new SimpleDateFormat(timeFormat);
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    public String getProjectName() {
        return projectConfig.projectName;
    }

    public void setProjectName(String projectName) {
        projectConfig.projectName = projectName;
    }

    public String getEndpoint() {
        return projectConfig.endpoint;
    }

    public void setEndpoint(String endpoint) {
        projectConfig.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return projectConfig.accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        projectConfig.accessKeyId = accessKeyId;
    }

    public String getAccessKey() {
        return projectConfig.accessKey;
    }

    public void setAccessKey(String accessKey) {
        projectConfig.accessKey = accessKey;
    }

    public String getStsToken() {
        return projectConfig.stsToken;
    }

    public void setStsToken(String stsToken) {
        projectConfig.stsToken = stsToken;
    }

    public int getPackageTimeoutInMS() {
        return config.packageTimeoutInMS;
    }

    public void setPackageTimeoutInMS(int packageTimeoutInMS) {
        config.packageTimeoutInMS = packageTimeoutInMS;
    }

    public int getLogsCountPerPackage() {
        return config.logsCountPerPackage;
    }

    public void setLogsCountPerPackage(int logsCountPerPackage) {
        config.logsCountPerPackage = logsCountPerPackage;
    }

    public int getLogsBytesPerPackage() {
        return config.logsBytesPerPackage;
    }

    public void setLogsBytesPerPackage(int logsBytesPerPackage) {
        config.logsBytesPerPackage = logsBytesPerPackage;
    }

    public int getMemPoolSizeInByte() {
        return config.memPoolSizeInByte;
    }

    public void setMemPoolSizeInByte(int memPoolSizeInByte) {
        config.memPoolSizeInByte = memPoolSizeInByte;
    }

    public int getIoThreadsCount() {
        return config.maxIOThreadSizeInPool;
    }

    public void setIoThreadsCount(int ioThreadsCount) {
        config.maxIOThreadSizeInPool = ioThreadsCount;
    }

    public int getShardHashUpdateIntervalInMS() {
        return config.shardHashUpdateIntervalInMS;
    }

    public void setShardHashUpdateIntervalInMS(int shardHashUpdateIntervalInMS) {
        config.shardHashUpdateIntervalInMS = shardHashUpdateIntervalInMS;
    }

    public int getRetryTimes() {
        return config.retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        config.retryTimes = retryTimes;
    }

}
