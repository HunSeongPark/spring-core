package hello.core.discount;

import hello.core.member.Member;

/**
 * Created by Hunseong on 2022/03/23
 */
public interface DiscountPolicy {

    /**
     * @return 할인액
     **/
    int discount(Member member, int price);
}
