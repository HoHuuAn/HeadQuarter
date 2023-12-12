package com.JavaTech.HeadQuarter.service.impl;

import com.JavaTech.HeadQuarter.model.Branch;
import com.JavaTech.HeadQuarter.model.User;
import com.JavaTech.HeadQuarter.repository.UserRepository;
import com.JavaTech.HeadQuarter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User saveOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void updateUserRole(Long userID, Long newRoleId) {
        userRepository.updateUserRole(userID, newRoleId);
    }

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findByBranch(Branch branch) {
        return userRepository.findByBranch(branch);
    }

}