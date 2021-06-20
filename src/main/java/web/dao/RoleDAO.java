package web.dao;

import web.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleDAO {
    List<Role> getRolesList();
    void save(Role role);
    void delete(Role role);
    Role getById(Long id);
    Role getRoleByName(String rolename);
    Role createRoleIfNotFound(String name, long id);
    public Set<Role> roleById(Long[] role_id);
}
