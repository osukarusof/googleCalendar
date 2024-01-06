package com.osukarusof.googleCalendar.repository;

import com.osukarusof.googleCalendar.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepostory extends JpaRepository<UserToken, Long> {

    @Query(value = "SELECT '*' FROM UserToken ut WHERE ut.user.id = :userId")
    Optional<UserToken> findByUserId(@Param("userId") Long userId);
}
