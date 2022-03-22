package hello.core.order;

/**
 * Created by Hunseong on 2022/03/23
 */
public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
