package com.ruoyi.project.system.dict.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ruoyi.framework.mapper.BaseMapper;
import com.ruoyi.project.system.dict.domain.DictType;

/**
 * 字典表 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface DictTypeMapper extends BaseMapper<DictType> {

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    List<DictType> selectDictTypeList(DictType dictType);

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    List<DictType> selectDictTypeAll();

    /**
     * 通过字典ID删除字典信息
     *
     * @param dictId 字典ID
     * @return 结果
     */
    int deleteDictTypeById(Long dictId);

    /**
     * 批量删除字典类型
     *
     * @param ids 需要删除的数据
     * @return 结果
     */
    int deleteDictTypeByIds(Long[] ids);

    /**
     * 新增字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    int insertDictType(DictType dictType);

    /**
     * 修改字典类型信息
     *
     * @param dictType 字典类型信息
     * @return 结果
     */
    int updateDictType(DictType dictType);

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    DictType checkDictTypeUnique(String dictType);
}
