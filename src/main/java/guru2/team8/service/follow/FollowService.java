package guru2.team8.service.follow;

import guru2.team8.service.follow.domain.Follow;
import guru2.team8.service.follow.repository.FollowRepository;
import guru2.team8.service.member.MemberService;
import guru2.team8.service.member.domain.dto.MemberResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberService memberService;

    public void follow(Long followingMemberId) {
        MemberResDto me = memberService.getMemberInfo();
        if (me.getId().equals(followingMemberId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        followRepository.findByFollowerIdAndFollowingId(me.getId(), followingMemberId)
                .ifPresent(existing -> {
                    throw new IllegalStateException("이미 팔로우 중입니다.");
                });

        followRepository.save(Follow.builder()
                .followerId(me.getId())
                .followingId(followingMemberId)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public void unfollow(Long followingMemberId) {
        MemberResDto me = memberService.getMemberInfo();
        Follow follow = followRepository.findByFollowerIdAndFollowingId(me.getId(), followingMemberId)
                .orElseThrow(() -> new IllegalStateException("팔로우 중이 아닙니다."));
        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public List<Long> getMyFollowingIds() {
        MemberResDto me = memberService.getMemberInfo();
        return followRepository.findFollowingIdsByFollowerId(me.getId());
    }
}

