package web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.RoleDAO;
import web.dao.UserDAO;
import web.model.Role;
import web.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private  UserDAO userDAO;
    private RoleDAO roleDAO;
    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }


    @Override
    public List<User> userList() {
        return userDAO.userList();
    }

    @Override
    public void save(User user, List<String> roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String st: roles) {
            roleSet.add(roleDAO.getRoleByName(st));
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleSet);
        userDAO.save(user);
    }


    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public User getById(Long id) {
        return userDAO.getById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDAO.getUserByName(username);
        return user;
    }

    @Override
    public Set<Role> getSetOfRoles(List<String> roles){
        Set<Role> roleSet = new HashSet<>();
        for (String st: roles) {
            roleSet.add(roleDAO.getRoleByName(st));
        }
        return roleSet;
    }
}
