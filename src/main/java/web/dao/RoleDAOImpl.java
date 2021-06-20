package web.dao;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Transactional
public class RoleDAOImpl implements RoleDAO{

    @PersistenceContext
    private EntityManager entityManager;

    public RoleDAOImpl() {
    }

    @Override
    public List<Role> getRolesList() {
        List<Role> resultList = entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
        return resultList;
    }

    @Transactional
    @Override
    public void save(Role role) {
        Role managed = entityManager.merge(role);
        entityManager.persist(managed);
    }


    @Override
    @Transactional
    public void delete(Role role) {
        Role managed = entityManager.merge(role);
        entityManager.remove(managed);
    }

    @Override
    public Role getById(Long id) {
        return entityManager.find(Role.class, id );
    }

    @Override

    public Role getRoleByName(String rolename) {
//        try{
            Role role = entityManager.createQuery("SELECT r FROM Role r where r.name = :name", Role.class)
                    .setParameter("name", rolename)
                    .getSingleResult();
            return role;
//        } catch (NoResultException ex){
//            return null;
//        }
    }


    public Role createRoleIfNotFound(String name, long id) {
        Role role = getRoleByName(name);
        if (role == null) {
            role = new Role(id, name);
            save(role);
        }
        return role;
    }


    @Override
    public Set<Role> roleById(Long[] role_id) {
        Set<Role> roleResult = new HashSet<>();
        for (Long id : role_id) {
            TypedQuery<Role> q = entityManager.createQuery("select role from Role role where role.id = :id", Role.class);
            q.setParameter("id", id);
            Role result = q.getResultList()
                    .stream()
                    .filter(role -> role.getId() == id)
                    .findAny()
                    .orElse(null);
            roleResult.add(result);
        }
        return roleResult;
    }
}
