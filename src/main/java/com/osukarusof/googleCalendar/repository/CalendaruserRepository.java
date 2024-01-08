package com.osukarusof.googleCalendar.repository;

import com.osukarusof.googleCalendar.entity.CalendarUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendaruserRepository extends JpaRepository<CalendarUser, Long> {
}
