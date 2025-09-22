package uz.oson.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.oson.taxi.entity.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
}
