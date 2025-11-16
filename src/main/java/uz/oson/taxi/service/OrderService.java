package uz.oson.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.Orders;
import uz.oson.taxi.repository.OrdersRepository;
import uz.oson.taxi.util.UpdateUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository ordersRepository;
    private final OrdersCacheService ordersCacheService;

    public void createOrderInCache(Update update) {
        Long chatId = UpdateUtil.getChatId(update);
        ordersCacheService.put(
                Orders
                        .builder()
                        .chatId(chatId)
                        .contactNumber(null)
                        .leavingDate(null)
                        .from_city(null)
                        .to_city(null)
                        .seatsCount(1)
                        .comment(null)
                        .build()
        );
    }

    public Orders findOrderByChatIdInCache(Long chatId) {
        return ordersCacheService.get(chatId);
    }

    public List<Orders> findAllOrdersByChatIdInDB(Long chatId) {
        return ordersRepository.findAllByChatId(chatId);
    }

    public void updateOrderInCache(Orders order) {
        ordersCacheService.put(order);
    }

    public void confirmOrder(Long chatId) {
        Orders order = ordersCacheService.get(chatId);
        if (order != null) {
            ordersRepository.save(order);
            ordersCacheService.evict(chatId);
        }
    }

    public void cancelOrder(Long chatId) {
        Orders order = ordersCacheService.get(chatId);
        if (order != null) {
            ordersCacheService.evict(chatId);
        }
    }
}
