package hello.core.member;

/**
 * Created by Hunseong on 2022/03/23
 */
public class MemberServiceImpl implements MemberService {

    // 보통 인터페이스의 구현체가 하나일 때 클래스명에 Impl을 붙임

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
