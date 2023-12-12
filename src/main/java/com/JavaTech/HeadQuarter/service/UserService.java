package com.JavaTech.HeadQuarter.service;

import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.Product;
import com.JavaTech.HeadQuarter.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User saveOrUpdate(User user);

    User findById(Long id);

    void updateUserRole(Long userID ,Long newRoleId);

    List<User> listAll();

    Optional<User> findByUsername(String username);

    void deleteById(Long id);

    List<User> findByBranch(Branch branch);
}
