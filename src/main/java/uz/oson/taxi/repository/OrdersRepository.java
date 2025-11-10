package uz.oson.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oson.taxi.entity.Orders;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findAllByChatId(Long chatId);
}
