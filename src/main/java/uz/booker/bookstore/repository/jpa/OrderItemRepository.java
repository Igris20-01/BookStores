package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.order.OrderItem;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {


    void deleteByOrderId(UUID orderId);

    List<OrderItem> findByOrderId(UUID orderId);

}

