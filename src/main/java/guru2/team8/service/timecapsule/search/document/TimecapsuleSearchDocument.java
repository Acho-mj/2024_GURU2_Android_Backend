package guru2.team8.service.timecapsule.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

@Document(indexName = "timecapsules")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimecapsuleSearchDocument {

    @Id
    private Long id; // timecapsule_id

    @Field(type = FieldType.Long)
    private Long memberId;

    @Field(type = FieldType.Keyword)
    private String username;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private String category;

    // 기존 DB 스키마가 String이라 그대로 유지 (ISO-8601 문자열)
    @Field(type = FieldType.Keyword)
    private String viewableAt;

    @GeoPointField
    @Field(name = "location")
    private GeoPoint location;
}

