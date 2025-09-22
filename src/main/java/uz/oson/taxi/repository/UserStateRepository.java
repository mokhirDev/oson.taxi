package uz.oson.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oson.taxi.entity.UserState;

import java.util.Optional;


@Repository
public interface UserStateRepository extends JpaRepository<UserState, Long> {
    Optional<UserState> findByChatId(Long chatId);

}
