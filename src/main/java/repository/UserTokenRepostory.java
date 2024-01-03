package repository;

import entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepostory extends JpaRepository<UserToken, Long> {
}
