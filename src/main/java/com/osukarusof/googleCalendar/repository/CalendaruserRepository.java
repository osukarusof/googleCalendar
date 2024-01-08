package com.osukarusof.googleCalendar.repository;

import com.osukarusof.googleCalendar.entity.CalendarUser;
import com.osukarusof.googleCalendar.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendaruserRepository extends JpaRepository<CalendarUser, Long> {

    @Query(value = "SELECT cu FROM CalendarUser cu WHERE cu.user.id = :userId")
    Optional<CalendarUser> findByUserId(@Param("userId") Long userId);
}
