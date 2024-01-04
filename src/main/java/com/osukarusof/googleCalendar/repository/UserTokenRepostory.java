package com.osukarusof.googleCalendar.repository;

import com.osukarusof.googleCalendar.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepostory extends JpaRepository<UserToken, Long> {
}
