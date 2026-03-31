package guru2.team8.service.timecapsule.search;

import guru2.team8.service.timecapsule.domain.CapsuleLocation;
import guru2.team8.service.timecapsule.domain.Timecapsule;
import guru2.team8.service.timecapsule.search.document.TimecapsuleSearchDocument;
import guru2.team8.service.timecapsule.search.repository.TimecapsuleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimecapsuleIndexService {

    private final TimecapsuleSearchRepository timecapsuleSearchRepository;

    @Transactional
    public void upsert(Timecapsule timecapsule, CapsuleLocation capsuleLocation) {
        if (timecapsule == null || timecapsule.getId() == null) return;
        if (capsuleLocation == null || capsuleLocation.getLatitude() == null || capsuleLocation.getLongitude() == null) return;

        TimecapsuleSearchDocument doc = TimecapsuleSearchDocument.builder()
                .id(timecapsule.getId())
                .memberId(timecapsule.getMemberId())
                .username(timecapsule.getUsername())
                .title(timecapsule.getTitle())
                .category(timecapsule.getCategory())
                .viewableAt(timecapsule.getViewableAt())
                .location(new GeoPoint(capsuleLocation.getLatitude(), capsuleLocation.getLongitude()))
                .build();

        timecapsuleSearchRepository.save(doc);
    }

    @Transactional
    public void deleteByTimecapsuleId(Long timecapsuleId) {
        if (timecapsuleId == null) return;
        timecapsuleSearchRepository.deleteById(timecapsuleId);
    }
}

