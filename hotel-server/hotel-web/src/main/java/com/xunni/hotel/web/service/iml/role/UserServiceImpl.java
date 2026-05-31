package com.xunni.hotel.web.service.iml.role;

import com.xunni.hotel.entity.AddRole;
import com.xunni.hotel.web.dto.role.AddRoleDoto;
import com.xunni.hotel.web.mapper.role.AddRoleMapper;
import com.xunni.hotel.web.service.role.addRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
public class UserServiceImpl implements addRoleService {

    @Autowired
    private AddRoleMapper addrolemapper;

    @Override
    @Transactional
    public void addRole(AddRoleDoto dto) {
        if(addrolemapper.countByRoleName(dto.getRoleName())>0){
            throw new RuntimeException("角色名称已存在");
        };

        AddRole role =new AddRole();

        role.setRoleName(dto.getRoleName());
        role.setRoleKey(dto.getRoleKey());
        role.setDescription(dto.getDescription());
        role.setStatus(dto.getStatus());
        role.setSortOrder(dto.getSortOrder());

        addrolemapper.insert(role);

    }



}
