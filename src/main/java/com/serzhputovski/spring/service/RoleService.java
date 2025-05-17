package com.serzhputovski.spring.service;

import com.serzhputovski.spring.entity.Role;

public interface RoleService {

    Role save(Role role);

    Role findByRoleName(String roleName);
}
