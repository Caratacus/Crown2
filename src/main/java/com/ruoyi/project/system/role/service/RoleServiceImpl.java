package com.ruoyi.project.system.role.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TypeUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.service.impl.BaseServiceImpl;
import com.ruoyi.project.system.role.domain.Role;
import com.ruoyi.project.system.role.domain.RoleDept;
import com.ruoyi.project.system.role.domain.RoleMenu;
import com.ruoyi.project.system.role.mapper.RoleMapper;
import com.ruoyi.project.system.user.domain.UserRole;
import com.ruoyi.project.system.user.service.IUserRoleService;

/**
 * 角色 业务层处理
 *
 * @author ruoyi
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IRoleMenuService roleMenuService;
    @Autowired
    private IRoleDeptService roleDeptService;
    @Autowired
    private IUserRoleService userRoleService;

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(tableAlias = "u")
    public List<Role> selectRoleList(Role role) {
        return baseMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRoleKeys(Long userId) {
        List<Role> perms = baseMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (Role perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> selectRolesByUserId(Long userId) {
        List<Role> userRoles = baseMapper.selectRolesByUserId(userId);
        List<Role> roles = selectRoleAll();
        for (Role role : roles) {
            for (Role userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @Override
    public List<Role> selectRoleAll() {
        return ((IRoleService) AopContext.currentProxy()).selectRoleList(new Role());
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public boolean deleteRoleById(Long roleId) {
        return delete().eq(Role::getRoleId, roleId).execute();
    }

    /**
     * 批量删除角色信息
     *
     * @param ids 需要删除的数据ID
     * @throws Exception
     */
    @Override
    public boolean deleteRoleByIds(String ids) {
        List<Long> roleIds = StringUtils.split2List(ids, TypeUtils::castToLong);
        for (Long roleId : roleIds) {
            Role role = getById(roleId);
            if (userRoleService.query().eq(UserRole::getRoleId, roleId).exist()) {
                throw new BusinessException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        return delete().inOrThrow(Role::getRoleId, roleIds).execute();
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean insertRole(Role role) {
        // 新增角色信息
        save(role);
        ShiroUtils.clearCachedAuthorizationInfo();
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean updateRole(Role role) {
        // 修改角色信息
        updateById(role);
        ShiroUtils.clearCachedAuthorizationInfo();
        // 删除角色与菜单关联
        roleMenuService.delete().eq(RoleMenu::getRoleId, role.getRoleId()).execute();
        return insertRoleMenu(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean authDataScope(Role role) {
        // 修改角色信息
        updateById(role);
        // 删除角色与部门关联
        roleDeptService.delete().eq(RoleDept::getRoleId, role.getRoleId()).execute();
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public boolean insertRoleMenu(Role role) {
        roleMenuService.saveBatch(
                Arrays.stream(role.getMenuIds()).map(menuId -> {
                    RoleMenu rm = new RoleMenu();
                    rm.setRoleId(role.getRoleId());
                    rm.setMenuId(menuId);
                    return rm;
                }).collect(Collectors.toList())
        );
        return true;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    private boolean insertRoleDept(Role role) {
        roleDeptService.saveBatch(
                Arrays.stream(role.getDeptIds()).map(deptId -> {
                    RoleDept rd = new RoleDept();
                    rd.setRoleId(role.getRoleId());
                    rd.setDeptId(deptId);
                    return rd;
                }).collect(Collectors.toList())
        );
        return true;
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(Role role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        Role info = query().eq(Role::getRoleName, role.getRoleName()).getOne();
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.ROLE_NAME_NOT_UNIQUE;
        }
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(Role role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        Role info = query().eq(Role::getRoleKey, role.getRoleKey()).getOne();
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 角色状态修改
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean changeStatus(Role role) {
        Role updateRole = new Role();
        updateRole.setStatus(role.getStatus());
        return update(updateRole, Wrappers.<Role>lambdaQuery().eq(Role::getRoleId, role.getRoleId()));

    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public boolean deleteAuthUser(UserRole userRole) {
        return userRoleService.delete().eq(UserRole::getRoleId, userRole.getRoleId()).eq(UserRole::getUserId, userRole.getUserId()).execute();
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public boolean deleteAuthUsers(Long roleId, String userIds) {
        return userRoleService.delete().eq(UserRole::getRoleId, roleId).inOrThrow(UserRole::getUserId, StringUtils.split2List(userIds)).execute();

    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public boolean insertAuthUsers(Long roleId, String userIds) {
        userRoleService.saveBatch(
                StringUtils.split2List(userIds, TypeUtils::castToLong).stream().map(userId -> {
                    UserRole ur = new UserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    return ur;
                }).collect(Collectors.toList())
        );
        return true;
    }

}
