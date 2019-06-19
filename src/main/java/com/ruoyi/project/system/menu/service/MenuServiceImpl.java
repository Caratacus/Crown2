package com.ruoyi.project.system.menu.service;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TreeUtils;
import com.ruoyi.common.utils.security.ShiroUtils;
import com.ruoyi.framework.service.impl.BaseServiceImpl;
import com.ruoyi.framework.web.domain.Ztree;
import com.ruoyi.project.system.menu.domain.Menu;
import com.ruoyi.project.system.menu.mapper.MenuMapper;
import com.ruoyi.project.system.role.domain.Role;
import com.ruoyi.project.system.user.domain.User;

/**
 * 菜单 业务层处理
 *
 * @author ruoyi
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements IMenuService {

    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    /**
     * 根据用户查询菜单
     *
     * @param user 用户信息
     * @return 菜单列表
     */
    @Override
    public List<Menu> selectMenusByUser(User user) {
        List<Menu> menus;
        // 管理员显示所有菜单信息
        if (user.isAdmin()) {
            menus = baseMapper.selectMenuNormalAll();
        } else {
            menus = baseMapper.selectMenusByUserId(user.getUserId());
        }
        return TreeUtils.getChildPerms(menus, 0);
    }

    /**
     * 查询菜单集合
     *
     * @return 所有菜单信息
     */
    @Override
    public List<Menu> selectMenuList(Menu menu) {

        List<Menu> menuList;
        User user = ShiroUtils.getSysUser();
        if (user.isAdmin()) {
            menuList = query().like(StringUtils.isNotEmpty(menu.getMenuName()), Menu::getMenuName, menu.getMenuName())
                    .eq(StringUtils.isNotEmpty(menu.getVisible()), Menu::getVisible, menu.getVisible())
                    .orderByAsc(Menu::getParentId)
                    .orderByAsc(Menu::getOrderNum)
                    .list();
        } else {
            menu.getParams().put("userId", user.getUserId());
            menuList = baseMapper.selectMenuListByUserId(menu);
        }
        return menuList;

    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectPermsByUserId(Long userId) {
        List<String> perms = baseMapper.selectPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(StringUtils.split2List(perm.trim()));
            }
        }
        return permsSet;
    }

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    @Override
    public List<Ztree> roleMenuTreeData(Role role) {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees;
        List<Menu> menuList = list();
        if (StringUtils.isNotNull(roleId)) {
            List<String> roleMenuList = baseMapper.selectMenuTree(roleId);
            ztrees = initZtree(menuList, roleMenuList, true);
        } else {
            ztrees = initZtree(menuList, null, true);
        }
        return ztrees;
    }

    /**
     * 查询所有菜单
     *
     * @return 菜单列表
     */
    @Override
    public List<Ztree> menuTreeData() {
        List<Menu> menuList = list();
        return initZtree(menuList);
    }

    @Override
    public List<Menu> list() {
        List<Menu> menuList;
        User user = ShiroUtils.getSysUser();
        if (user.isAdmin()) {
            menuList = query().orderByAsc(Menu::getParentId).orderByAsc(Menu::getOrderNum).list();
        } else {
            menuList = baseMapper.selectMenuAllByUserId(user.getUserId());
        }
        return menuList;


    }

    /**
     * 查询系统所有权限
     *
     * @return 权限列表
     */
    @Override
    public LinkedHashMap<String, String> selectPermsAll() {
        return list().stream()
                .collect(
                        Collectors.toMap(Menu::getUrl, v -> MessageFormat.format(PREMISSION_STRING, v.getPerms()),
                                (k, v) -> {
                                    throw new IllegalStateException(String.format("Duplicate key %s", k));
                                }, LinkedHashMap::new)
                );
    }

    /**
     * 对象转菜单树
     *
     * @param menuList 菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<Menu> menuList) {
        return initZtree(menuList, null, false);
    }

    /**
     * 对象转菜单树
     *
     * @param menuList     菜单列表
     * @param roleMenuList 角色已存在菜单列表
     * @param permsFlag    是否需要显示权限标识
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<Menu> menuList, List<String> roleMenuList, boolean permsFlag) {
        boolean isCheck = StringUtils.isNotNull(roleMenuList);
        return menuList.stream().map(menu -> {
            Ztree ztree = new Ztree();
            ztree.setId(menu.getMenuId());
            ztree.setpId(menu.getParentId());
            ztree.setName(transMenuName(menu, permsFlag));
            ztree.setTitle(menu.getMenuName());
            if (isCheck) {
                ztree.setChecked(roleMenuList.contains(menu.getMenuId() + menu.getPerms()));
            }
            return ztree;
        }).collect(Collectors.toList());
    }

    private String transMenuName(Menu menu, boolean permsFlag) {
        StringBuilder sb = new StringBuilder(menu.getMenuName());
        if (permsFlag) {
            sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;").append(menu.getPerms()).append("</font>");
        }
        return sb.toString();
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public boolean deleteMenuById(Long menuId) {
        ShiroUtils.clearCachedAuthorizationInfo();
        return delete().and(e -> e.eq(Menu::getMenuId, menuId).or().eq(Menu::getParentId, menuId)).execute();
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public Menu selectMenuById(Long menuId) {
        return baseMapper.selectMenuById(menuId);
    }


    /**
     * 新增保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean insertMenu(Menu menu) {
        ShiroUtils.clearCachedAuthorizationInfo();
        return save(menu);
    }

    /**
     * 修改保存菜单信息
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public boolean updateMenu(Menu menu) {
        ShiroUtils.clearCachedAuthorizationInfo();
        return updateById(menu);
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(Menu menu) {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        Menu info = query().eq(Menu::getMenuName, menu.getMenuName()).eq(Menu::getParentId, menu.getParentId()).getOne();
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return UserConstants.MENU_NAME_NOT_UNIQUE;
        }
        return UserConstants.MENU_NAME_UNIQUE;
    }

}
