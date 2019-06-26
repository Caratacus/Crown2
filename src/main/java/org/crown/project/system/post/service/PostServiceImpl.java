package org.crown.project.system.post.service;

import java.util.List;

import org.crown.common.constant.UserConstants;
import org.crown.common.exception.BusinessException;
import org.crown.common.utils.StringUtils;
import org.crown.common.utils.TypeUtils;
import org.crown.framework.service.impl.BaseServiceImpl;
import org.crown.project.system.post.domain.Post;
import org.crown.project.system.post.mapper.PostMapper;
import org.crown.project.system.user.domain.UserPost;
import org.crown.project.system.user.service.IUserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 岗位信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<PostMapper, Post> implements IPostService {

    @Autowired
    private IUserPostService userPostService;

    @Override
    public List<Post> selectPostList(Post post) {
        return query().like(StringUtils.isNotEmpty(post.getPostCode()), Post::getPostCode, post.getPostCode())
                .eq(StringUtils.isNotEmpty(post.getStatus()), Post::getStatus, post.getStatus())
                .like(StringUtils.isNotEmpty(post.getPostName()), Post::getPostName, post.getPostName())
                .list();
    }

    @Override
    public List<Post> selectAllPostsByUserId(Long userId) {
        List<Post> userPosts = selectPostsByUserId(userId);
        List<Post> posts = list();
        for (Post post : posts) {
            for (Post userRole : userPosts) {
                if (post.getPostId().longValue() == userRole.getPostId().longValue()) {
                    post.setFlag(true);
                    break;
                }
            }
        }
        return posts;
    }

    @Override
    public List<Post> selectPostsByUserId(Long userId) {
        return baseMapper.selectPostsByUserId(userId);
    }

    @Override
    public boolean deletePostByIds(String ids) {
        List<Long> postIds = StringUtils.split2List(ids, TypeUtils::castToLong);
        for (Long postId : postIds) {
            Post post = getById(postId);
            if (userPostService.query().eq(UserPost::getPostId, postId).exist()) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        return delete().inOrThrow(Post::getPostId, postIds).execute();
    }

    @Override
    public String checkPostNameUnique(Post post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        Post info = query().eq(Post::getPostName, post.getPostName()).getOne();
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.POST_NAME_NOT_UNIQUE;
        }
        return UserConstants.POST_NAME_UNIQUE;
    }

    @Override
    public String checkPostCodeUnique(Post post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        Post info = query().eq(Post::getPostCode, post.getPostCode()).getOne();
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.POST_CODE_NOT_UNIQUE;
        }
        return UserConstants.POST_CODE_UNIQUE;
    }
}
