package org.crown.project.system.dict.service;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.crown.common.utils.StringUtils;
import org.crown.common.utils.TypeUtils;
import org.crown.framework.exception.MsgException;
import org.crown.framework.service.impl.BaseServiceImpl;
import org.crown.project.system.dict.domain.DictType;
import org.crown.project.system.dict.mapper.DictTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

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
                throw new MsgException(HttpServletResponse.SC_BAD_REQUEST, dictType.getDictName() + "已分配，不能删除");
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
    public boolean checkDictTypeUnique(DictType dict) {
        Long dictId = dict.getDictId();
        DictType info = query().eq(DictType::getDictType, dict.getDictType()).getOne();
        return Objects.isNull(info) || info.getDictId().equals(dictId);
    }

}
