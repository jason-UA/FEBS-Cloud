package com.febs.auth.manager;

import com.febs.auth.mapper.MenuMapper;
import com.febs.auth.mapper.UserMapper;
import com.febs.common.entity.system.Menu;
import com.febs.common.entity.system.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Usermanager {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    public SystemUser findByName(String username) {
        return userMapper.findByName(username);
    }

    public String findUserPermissions(String username) {
        List<Menu> userPermissions = menuMapper.findUserPermissions(username);

        return userPermissions.stream().map(Menu::getPerms).collect(Collectors.joining(","));
    }
}
