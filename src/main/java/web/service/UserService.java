package web.service;

import web.model.Role;
import web.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    Set<Role> getSetOfRoles(List<String> rolesId);
    List<User> userList();
    void save(User user);
    void delete(User user);
    User getById(Long id);

}
