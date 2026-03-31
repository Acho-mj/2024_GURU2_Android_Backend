package guru2.team8.service.timecapsule.search.repository;

import guru2.team8.service.timecapsule.search.document.TimecapsuleSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimecapsuleSearchRepository extends ElasticsearchRepository<TimecapsuleSearchDocument, Long> {
}

