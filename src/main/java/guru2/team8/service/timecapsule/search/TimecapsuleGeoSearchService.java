package guru2.team8.service.timecapsule.search;

import guru2.team8.service.timecapsule.domain.dto.TimecapsuleMapDto;
import guru2.team8.service.timecapsule.search.document.TimecapsuleSearchDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TimecapsuleGeoSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ZonedDateTime nowInSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

    public List<TimecapsuleMapDto> searchWithinRadius(
            double latitude,
            double longitude,
            double radiusMeters,
            Set<Long> allowedMemberIds,
            String category,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.min(Math.max(1, size), 200));

        Query q = new Query.Builder().bool(b -> {
            b.filter(f -> f.geoDistance(g -> g
                    .field("location")
                    .distance(radiusMeters + "m")
                    .location(l -> l.latlon(ll -> ll.lat(latitude).lon(longitude)))));

            if (allowedMemberIds != null && !allowedMemberIds.isEmpty()) {
                b.filter(f -> f.terms(t -> t
                        .field("memberId")
                        .terms(v -> v.value(allowedMemberIds.stream()
                                .map(co.elastic.clients.elasticsearch._types.FieldValue::of)
                                .toList()))));
            }

            if (category != null && !category.isBlank() && !"전체".equals(category)) {
                b.filter(f -> f.term(t -> t.field("category").value(category)));
            }

            return b;
        }).build();

        NativeQuery query = NativeQuery.builder()
                .withQuery(q)
                .withPageable(pageable)
                .build();

        SearchHits<TimecapsuleSearchDocument> hits = elasticsearchOperations.search(query, TimecapsuleSearchDocument.class);

        return hits.getSearchHits().stream()
                .map(hit -> toMapDto(hit, latitude, longitude))
                .sorted((a, b) -> {
                    if (a.getDistanceMeters() == null && b.getDistanceMeters() == null) return 0;
                    if (a.getDistanceMeters() == null) return 1;
                    if (b.getDistanceMeters() == null) return -1;
                    return Double.compare(a.getDistanceMeters(), b.getDistanceMeters());
                })
                .toList();
    }

    private TimecapsuleMapDto toMapDto(SearchHit<TimecapsuleSearchDocument> hit, double userLat, double userLon) {
        TimecapsuleSearchDocument doc = hit.getContent();
        GeoPoint loc = doc.getLocation();

        Double distanceMeters = null;
        if (loc != null) {
            distanceMeters = haversineMeters(userLat, userLon, loc.getLat(), loc.getLon());
        }

        String viewableDate = LocalDateTime.parse(doc.getViewableAt()).toLocalDate().format(dateFormatter);
        long daysLeft = ChronoUnit.DAYS.between(nowInSeoul.toLocalDate(), LocalDateTime.parse(doc.getViewableAt()).toLocalDate());
        if (daysLeft < 0) daysLeft = 0;

        return new TimecapsuleMapDto(
                doc.getId(),
                doc.getMemberId(),
                doc.getUsername(),
                doc.getTitle(),
                doc.getCategory(),
                viewableDate,
                loc != null ? loc.getLat() : null,
                loc != null ? loc.getLon() : null,
                daysLeft,
                distanceMeters
        );
    }

    private static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371000.0; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

