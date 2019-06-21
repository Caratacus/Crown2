package org.crown.project.system.dict.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.crown.common.constant.UserConstants;
import org.crown.common.exception.BusinessException;
import org.crown.common.utils.StringUtils;
import org.crown.common.utils.TypeUtils;
import org.crown.framework.service.impl.BaseServiceImpl;
import org.crown.project.system.dict.domain.DictType;
import org.crown.project.system.dict.mapper.DictTypeMapper;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class DictTypeServiceImpl extends BaseServiceImpl<DictTypeMapper, DictType> implements IDictTypeService {

    @Override
    public List<DictType> selectDictTypeList(DictType dictType) {
        String beginTime = TypeUtils.castToString(dictType.getParams().get("beginTime"));
        String endTime = TypeUtils.castToString(dictType.getParams().get("endTime"));
        return query().like(StringUtils.isNotEmpty(dictType.getDictName()), DictType::getDictName, dictType.getDictName())
                .eq(StringUtils.isNotEmpty(dictType.getStatus()), DictType::getStatus, dictType.getStatus())
                .like(StringUtils.isNotEmpty(dictType.getDictType()), DictType::getDictType, dictType.getDictType())
                .gt(StringUtils.isNotEmpty(beginTime), DictType::getCreateTime, beginTime)
                .lt(StringUtils.isNotEmpty(endTime), DictType::getCreateTime, endTime)
                .list();
    }

    @Override
    public boolean deleteDictTypeByIds(String ids) {
        List<Long> dictIds = StringUtils.split2List(ids, TypeUtils::castToLong);
        for (Long dictId : dictIds) {
            DictType dictType = getById(dictId);
            if (query().eq(DictType::getDictType, dictType.getDictType()).exist()) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
        }
        return delete().inOrThrow(DictType::getDictId, dictIds).execute();
    }

    @Override
    @Transactional
    public boolean updateDictType(DictType dictType) {
        DictType oldDict = getById(dictType.getDictId());
        DictType updateDictType = new DictType();
        updateDictType.setDictType(dictType.getDictType());
        update(updateDictType, Wrappers.<DictType>lambdaQuery().eq(DictType::getDictType, oldDict.getDictType()));
        return updateById(dictType);
    }

    @Override
    public String checkDictTypeUnique(DictType dict) {
        Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        DictType dictType = query().eq(DictType::getDictType, dict.getDictType()).getOne();
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue()) {
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }
}
