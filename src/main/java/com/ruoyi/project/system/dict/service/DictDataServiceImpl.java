package com.ruoyi.project.system.dict.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TypeUtils;
import com.ruoyi.framework.service.impl.BaseServiceImpl;
import com.ruoyi.project.system.dict.domain.DictData;
import com.ruoyi.project.system.dict.mapper.DictDataMapper;

/**
 * 字典 业务层处理
 *
 * @author ruoyi
 */
@Service
public class DictDataServiceImpl extends BaseServiceImpl<DictDataMapper, DictData> implements IDictDataService {

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<DictData> selectDictDataList(DictData dictData) {
        return query().eq(StringUtils.isNotEmpty(dictData.getDictType()), DictData::getDictType, dictData.getDictType())
                .like(StringUtils.isNotEmpty(dictData.getDictLabel()), DictData::getDictLabel, dictData.getDictLabel())
                .eq(StringUtils.isNotEmpty(dictData.getStatus()), DictData::getStatus, dictData.getStatus())
                .list();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Override
    public List<DictData> selectDictDataByType(String dictType) {
        return query().eq(DictData::getDictType, dictType).eq(DictData::getStatus, "0").orderByAsc(DictData::getDictSort).list();
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return query().select(DictData::getDictLabel).eq(DictData::getDictType, dictType).eq(DictData::getDictValue, dictValue).getObj(TypeUtils::castToString);
    }

}
