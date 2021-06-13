package web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/")
public class AdminController {
//   @Autowired
    private final UserService userService;

// @Autowired
    private final RoleDAO roleDAO;

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

        @Autowired
    public AdminController(UserService userService, RoleDAO roleDAO) {
        this.userService = userService;
        this.roleDAO = roleDAO;
    }

    @GetMapping("/admin")
    public ModelAndView allUsers() {
        List<User> users = userService.userList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("usersList", users);
        return modelAndView;
    }


//    @GetMapping("/admin/new")
//    public String newUserForm(Model model) {
//        model.addAttribute(new User());
//        List<Role> roles = roleService.getRolesList();
//        model.addAttribute("allRoles", roles);
//        return "add";
//    }
//
//    @PostMapping("admin/adduser")
//    public String addUser(@Validated(User.class) @ModelAttribute("user") User user,
//                          @RequestParam("authorities") List<String> values,
//                          BindingResult result) {
//        if(result.hasErrors()) {
//            return "redirect:/admin/adduser";
//        }
//        Set<Role> roleSet = userService.getSetOfRoles(values);
//        user.setRoles(roleSet);
//        userService.save(user);
//        return "redirect:/admin";
//    }


    @GetMapping(value = "/admin/new")
    public ModelAndView addPage() {
        User user = new User();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add");
       modelAndView.addObject("user", user);
        List<Role> roles = roleService.getRolesList();
        modelAndView.addObject("roleSet", roles);
        return modelAndView;
    }

    @PostMapping(value = "/admin/add")
    public String addUser(
            @ModelAttribute User user,
            @RequestParam ("roles") List<String> authorities
                          ) {

        userService.save(user, authorities);
        return "redirect:/admin";
    }

//    @ModelAttribute("name") String name,
//    @ModelAttribute("password") String password,
//    @ModelAttribute("lastName") String lastName,
//    @ModelAttribute("age") byte age,
//    @ModelAttribute("email") String email,
//    @RequestParam("roles") String[] roles
//    ) {
//        User userToAdd = new User();
//        userToAdd.setName(user.getName());
//        userToAdd.setLastName(user.getLastName());
//        userToAdd.setAge(user.getAge());
//        userToAdd.setEmail(user.getEmail());
//        userToAdd.setPassword(user.getPassword());
//

//        Set<Role> setRoles = new HashSet<>();
//        for (String st : roles) {
//            setRoles.add(roleDAO.getRoleByName(st));
//        }
//        user.setRoles(setRoles);
//        userService.save(user);
//        return "redirect:/admin";
//    }


    @GetMapping(value = "/admin/edit/{id}")
    public ModelAndView editPage(@PathVariable("id") long id) {
        User user = userService.getById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit");
        modelAndView.addObject("user", user);
        Set<Role> Setroles = new HashSet<>();
        Setroles.add(roleDAO.getRoleByName("ADMIN"));
        Setroles.add(roleDAO.getRoleByName("USER"));
        modelAndView.addObject("rolelist", Setroles);
        return modelAndView;
    }

    @PostMapping(value = "/admin/edit")
    public String editUser(
            @ModelAttribute("id") Long id,
            @ModelAttribute("name") String name,
            @ModelAttribute("password") String password,
            @ModelAttribute("lastName") String lastName,
            @ModelAttribute("age") byte age,
            @ModelAttribute("email") String email,
            @RequestParam("roles") List<String> authorities
    ) {
        User user = userService.getById(id);
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        if (!password.isEmpty()) {
            user.setPassword(password);
        }
        Set<Role> roleSet = new HashSet<>();
        for (String st : authorities) {
            roleSet.add(roleDAO.getRoleByName(st));
        }
        user.setRoles(roleSet);
        userService.save(user, authorities);
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        User user = userService.getById(id);
        userService.delete(user);
        return "redirect:/admin";
    }

}
