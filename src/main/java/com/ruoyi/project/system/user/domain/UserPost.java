package com.ruoyi.project.system.user.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author ruoyi
 */
@Setter
@Getter
public class UserPost {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 岗位ID
     */
    private Long postId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("postId", getPostId())
                .toString();
    }
}
