package hello.core.member;

/**
 * Created by Hunseong on 2022/03/23
 */
public interface MemberRepository {

    void save(Member member);

    Member findById(Long memberId);
}
