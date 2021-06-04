package web.dao;

import web.model.User;

import java.util.List;

public interface UserDAO {
    List<User> userList();
    void save(User user);
    void delete(User user);
    User getById(Long id);
    User getUserByName(String username);
}
