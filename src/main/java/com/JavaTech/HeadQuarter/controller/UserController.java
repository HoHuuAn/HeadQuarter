package com.JavaTech.HeadQuarter.controller;

import com.JavaTech.HeadQuarter.dto.UserDTO;
import com.JavaTech.HeadQuarter.model.ERole;
import com.JavaTech.HeadQuarter.model.Role;
import com.JavaTech.HeadQuarter.model.User;
import com.JavaTech.HeadQuarter.repository.RoleRepository;
import com.JavaTech.HeadQuarter.service.BranchService;
import com.JavaTech.HeadQuarter.service.UserService;
import com.JavaTech.HeadQuarter.utils.ImageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/list")
    public String showUser(Model model){

        List<UserDTO> userDTOList = userService.listAll().stream()
                .map(user -> {
                    UserDTO userdto =  modelMapper.map(user, UserDTO.class);
                    userdto.setRoleOfUser(getRoles(user.getRoles()));
                    return userdto;
                })
                .collect(Collectors.toList());

        model.addAttribute("userList", userDTOList);
        model.addAttribute("branchList", branchService.listAll());
        return "/user/page-list-users";
    }

    public String getRoles(Set<Role> roles ){
        Role firstRole = roles.iterator().next();
        return firstRole.getName().toString();
    }

    @PostMapping(value = "/get-users-by-brands")
    public ResponseEntity<?> getUsers(@RequestParam("branch") String branch){
        Map<String, Object> response = new HashMap<>();
        response.put("userList", userService.findByBranch(branchService.findByName(branch)));
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/add")
    public String showAdd(Model model){
        model.addAttribute("branchList", branchService.listAll());
        return "/user/page-add-users";
    }

    @PostMapping(value = "/add")
    public String addUser(@RequestParam(name = "avatar", required = false) MultipartFile avatar,
                          @RequestParam("fullName") String fullName,
                          @RequestParam("phone") String phone,
                          @RequestParam("email") String email,
                          @RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("gender") String gender,
                          @RequestParam("status") String status,
                          @RequestParam("branch") String branch) throws IOException {
        //role
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();
        roles.add((status.equals("Admin"))?adminRole:userRole);

        User user = User.builder()
                .avatar(ImageUtil.convertToBase64(avatar))
                .fullName(fullName)
                .phone(phone)
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .gender(gender)
                .roles(roles)
                .branch(branchService.findByName(branch))
                .activated(true)
                .unlocked(true).build();
        userService.saveOrUpdate(user);
        return "redirect:/user/list";
    }

    @PostMapping(value = "/change-role")
    private ResponseEntity<?> changeRole(@RequestParam("userId") Long id,
                                         @RequestParam("role") String role){
        User user = userService.findById(id);

        if (user.getRoles().iterator().next().getId() == 1L){
            userService.updateUserRole(user.getId(), 2L);
        }
        if (user.getRoles().iterator().next().getId() == 2L){
            userService.updateUserRole(user.getId(), 1L);
        }
        Map<String, Object> response = new HashMap<>();
        return ResponseEntity.ok(response);
    }
}
