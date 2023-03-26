package com.example.library_management.repository;

import com.example.library_management.entity.BannedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface BanList extends JpaRepository<BannedUser, Long> {
    Optional<BannedUser> findBannedUserByUserEmail(String Email);
}
