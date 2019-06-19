package com.ruoyi.project.system.dict.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.common.utils.text.Convert;
import com.ruoyi.framework.service.impl.BaseServiceImpl;
import com.ruoyi.project.system.dict.domain.DictType;
import com.ruoyi.project.system.dict.mapper.DictDataMapper;
import com.ruoyi.project.system.dict.mapper.DictTypeMapper;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class DictTypeServiceImpl extends BaseServiceImpl<DictTypeMapper, DictType> implements IDictTypeService {


    @Autowired
    private DictDataMapper dictDataMapper;

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<DictType> selectDictTypeList(DictType dictType) {
        return baseMapper.selectDictTypeList(dictType);
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<DictType> selectDictTypeAll() {
        return baseMapper.selectDictTypeAll();
    }

    /**
     * 通过字典ID删除字典信息
     *
     * @param dictId 字典ID
     * @return 结果
     */
    @Override
    public int deleteDictTypeById(Long dictId) {
        return baseMapper.deleteDictTypeById(dictId);
    }

    /**
     * 批量删除字典类型
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    @Override
    public int deleteDictTypeByIds(String ids) {
        Long[] dictIds = Convert.toLongArray(ids);
        for (Long dictId : dictIds) {
            DictType dictType = getById(dictId);
            if (query().eq(DictType::getDictType, dictType.getDictType()).exist()) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
        }

        return baseMapper.deleteDictTypeByIds(dictIds);
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    public int insertDictType(DictType dictType) {
        dictType.setCreateBy(ShiroUtils.getLoginName());
        return baseMapper.insertDictType(dictType);
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDictType(DictType dictType) {
        dictType.setUpdateBy(ShiroUtils.getLoginName());
        DictType oldDict = getById(dictType.getDictId());
        DictType updateDictType = new DictType();
        updateDictType.setDictType(dictType.getDictType());
        update(updateDictType, Wrappers.<DictType>lambdaQuery().eq(DictType::getDictType, oldDict.getDictType()));
        return baseMapper.updateDictType(dictType);
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(DictType dict) {
        Long dictId = StringUtils.isNull(dict.getDictId()) ? -1L : dict.getDictId();
        DictType dictType = baseMapper.checkDictTypeUnique(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue()) {
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }
}
