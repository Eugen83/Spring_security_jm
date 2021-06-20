package web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.dao.RoleDAO;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.RoleServiceImpl;
import web.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
//   @Autowired
    private UserService userService;

// @Autowired
    private RoleDAO roleDAO;

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

//
//    @Autowired
//    PasswordEncoder bCryptPasswordEncoder;

        @Autowired
    public AdminController(UserService userService, RoleDAO roleDAO) {
        this.userService = userService;
        this.roleDAO = roleDAO;
    }

    @GetMapping()
    public ModelAndView allUsers() {
        List<User> users = userService.userList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("usersList", users);
        return modelAndView;
    }


        @GetMapping(value = "/add")
    public ModelAndView addPage() {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("add");
            modelAndView.addObject("user", new User());
            List<Role> roles = roleService.getAllRole();
            modelAndView.addObject("rolelist", roles);
            return modelAndView;
        }

    @PostMapping()
    public String addUser(@ModelAttribute("user") User user,
                           @RequestParam(required = false, name = "role_id", defaultValue = "2") Long[] role_id) {
        user.setRoles(roleService.roleById(role_id));
        userService.save(user);
        return "redirect:/admin";
    }




    @GetMapping(value = "/edit/{id}")
    public ModelAndView editPage(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit");
        modelAndView.addObject("user", user);
        List<Role> roles = roleService.getAllRole();
        modelAndView.addObject("rolelist", roles);
        return modelAndView;
    }

    @PostMapping(value = "/edit")
    public String editUser(
            @ModelAttribute User user,
            @RequestParam(defaultValue = "2", required = false, name = "role_id") Long [] role_id
    ) {
           user.setRoles(roleService.roleById(role_id));
            userService.save(user);
        return "redirect:/admin";
    }



    @GetMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        userService.delete(user);
        return "redirect:/admin";
    }

}
