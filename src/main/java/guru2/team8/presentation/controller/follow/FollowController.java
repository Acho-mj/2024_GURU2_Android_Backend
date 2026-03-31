package guru2.team8.presentation.controller.follow;

import guru2.team8.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{followingMemberId}")
    public ResponseEntity<Void> follow(@PathVariable Long followingMemberId) {
        followService.follow(followingMemberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followingMemberId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long followingMemberId) {
        followService.unfollow(followingMemberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/following-ids")
    public ResponseEntity<List<Long>> myFollowingIds() {
        return ResponseEntity.ok(followService.getMyFollowingIds());
    }
}

