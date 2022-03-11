package com.cheng.service.role;

import com.cheng.pojo.Role;

import java.util.List;

public interface RoleService {
    // 获取角色列表:业务层 中转站 返回值类型和RoleDao一样
    public List<Role> getRoleList();
}
