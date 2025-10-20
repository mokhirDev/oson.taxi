package uz.oson.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.oson.taxi.entity.Orders;
import uz.oson.taxi.repository.OrdersRepository;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository ordersRepository;
    private final OrdersCacheService ordersCacheService;

    public void createOrder(Update update) {
        ordersCacheService.put(
                Orders
                        .builder()
                        .chatId(update.getMessage().getChatId())
                        .contactNumber(update.getMessage().getContact().getPhoneNumber())
                        .leavingDate(null)
                        .from_city(null)
                        .to_city(null)
                        .seatsCount(0)
                        .comment(null)
                        .build()
        );
    }

    public Orders findOrderByChatId(Long chatId) {
        return ordersCacheService.get(chatId);
    }

    public void updateOrder(Orders order) {
        ordersCacheService.put(order);
    }

}
