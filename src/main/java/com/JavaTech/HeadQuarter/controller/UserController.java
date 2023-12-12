package com.JavaTech.HeadQuarter.controller;

import com.JavaTech.HeadQuarter.dto.UserDTO;
import com.JavaTech.HeadQuarter.model.Role;
import com.JavaTech.HeadQuarter.model.User;
import com.JavaTech.HeadQuarter.service.BranchService;
import com.JavaTech.HeadQuarter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private ModelMapper modelMapper;

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
        model.addAttribute("userList", userService.listAll());
        return "/user/page-add-users";
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
