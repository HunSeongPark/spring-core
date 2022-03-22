package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

/**
 * Created by Hunseong on 2022/03/23
 */
public class FixDiscountPolicy implements DiscountPolicy {

    private int discountFixAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
