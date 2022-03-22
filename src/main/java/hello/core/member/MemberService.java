package hello.core.member;

/**
 * Created by Hunseong on 2022/03/23
 */
public interface MemberService {

    void join(Member member);

    Member findMember(Long memberId);
}
