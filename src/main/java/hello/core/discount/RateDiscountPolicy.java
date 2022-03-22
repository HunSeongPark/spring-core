package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.stereotype.Component;

/**
 * Created by Hunseong on 2022/03/23
 */
@Component
public class RateDiscountPolicy implements DiscountPolicy {

    private int discountPercent = 10; // 10% discount

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;
        } else {
            return 0;
        }
    }
}
