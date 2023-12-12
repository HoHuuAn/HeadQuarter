package com.JavaTech.HeadQuarter.repository;

import com.JavaTech.HeadQuarter.model.ConfirmationToken;
import com.JavaTech.HeadQuarter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

    ConfirmationToken findConfirmationTokenByUserEntity(User user);
}
