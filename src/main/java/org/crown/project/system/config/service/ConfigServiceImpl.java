package org.crown.project.system.config.service;

import java.util.List;
import java.util.Objects;

import org.crown.common.constant.UserConstants;
import org.crown.common.utils.StringUtils;
import org.crown.common.utils.TypeUtils;
import org.crown.framework.service.impl.BaseServiceImpl;
import org.crown.project.system.config.domain.Config;
import org.crown.project.system.config.mapper.ConfigMapper;
import org.springframework.stereotype.Service;

/**
 * 参数配置 服务层实现
 *
 * @author ruoyi
 */
@Service
public class ConfigServiceImpl extends BaseServiceImpl<ConfigMapper, Config> implements IConfigService {

    @Override
    public Config selectConfigByKey(String configKey) {
        return query().eq(Config::getConfigKey, configKey).getOne();
    }

    @Override
    public List<Config> selectConfigList(Config config) {
        String beginTime = TypeUtils.castToString(config.getParams().get("beginTime"));
        String endTime = TypeUtils.castToString(config.getParams().get("endTime"));
        return query().like(StringUtils.isNotEmpty(config.getConfigName()), Config::getConfigName, config.getConfigName())
                .eq(StringUtils.isNotEmpty(config.getConfigType()), Config::getConfigType, config.getConfigType())
                .like(StringUtils.isNotEmpty(config.getConfigKey()), Config::getConfigKey, config.getConfigKey())
                .gt(StringUtils.isNotEmpty(beginTime), Config::getCreateTime, beginTime)
                .lt(StringUtils.isNotEmpty(endTime), Config::getCreateTime, endTime)
                .list();
    }

    @Override
    public String checkConfigKeyUnique(Config config) {
        Long configId = Objects.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        Config info = selectConfigByKey(config.getConfigKey());
        if (Objects.nonNull(info) && info.getConfigId().longValue() != configId.longValue()) {
            return UserConstants.CONFIG_KEY_NOT_UNIQUE;
        }
        return UserConstants.CONFIG_KEY_UNIQUE;
    }

    @Override
    public String getConfigValueByKey(String configkey) {
        Config config = selectConfigByKey(configkey);
        return StringUtils.isNotNull(config) ? config.getConfigValue() : "";
    }
}