package org.crown.project.system.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.crown.project.system.post.domain.Post;

import org.crown.framework.mapper.BaseMapper;

/**
 * 岗位信息 数据层
 *
 * @author ruoyi
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 根据用户ID查询岗位
     *
     * @param userId 用户ID
     * @return 岗位列表
     */
    List<Post> selectPostsByUserId(Long userId);

}
