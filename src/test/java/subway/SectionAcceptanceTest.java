package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.controller.dto.line.LineSaveRequest;
import subway.controller.dto.section.SectionSaveRequest;
import subway.controller.dto.station.StationResponse;
import subway.utils.LineApiHelper;
import subway.utils.StationApiHelper;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.utils.SectionApiHelper.callApiToCreateSection;

@Sql("truncate_tables.sql")
@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private Long stationId_A;
    private Long stationId_B;
    private Long stationId_C;
    private Long stationId_D;
    private String stationName_A = "지하철역 A";
    private String stationName_B = "지하철역 B";
    private String stationName_C = "지하철역 C";
    private String stationName_D = "지하철역 D";

    @BeforeEach
    public void init() {

        stationId_A = StationApiHelper.callApiToCreateStation(stationName_A)
                                      .jsonPath()
                                      .getLong("id");
        stationId_B = StationApiHelper.callApiToCreateStation(stationName_B)
                                      .jsonPath()
                                      .getLong("id");
        stationId_C = StationApiHelper.callApiToCreateStation(stationName_C)
                                      .jsonPath()
                                      .getLong("id");
        stationId_D = StationApiHelper.callApiToCreateStation(stationName_D)
                                      .jsonPath()
                                      .getLong("id");
    }

    /**
     * Given 상행종점역이 A이고, 하행종점역이 B인 지하철 노선을 생성하고
     * When 지하철역 B와 C를 연결하는 구간을 생성하면
     * Then 해당 노선이 A-B-C로 연결되고,
     * Then 지하철 노선의 하행역이 C로 바뀐다.
     */
    @DisplayName("2개역이 있는 노선에 지하철 구간을 추가한다.")
    @Test
    void createSection_성공__2개역_노선() {

        // given
        Long lineId = createLine(stationId_A, stationId_B);

        // when
        ExtractableResponse<Response> creationResponse = createSection(lineId, stationId_B, stationId_C);

        // then
        assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<StationResponse> stations = LineApiHelper.callApiToGetSingleLine(lineId)
                                                      .jsonPath()
                                                      .getList("stations", StationResponse.class);
        assertThat(stations.size()).isEqualTo(3);
        assertThat(stations.stream()
                           .map(StationResponse::getId)
                           .collect(Collectors.toList())).contains(stationId_A, stationId_B, stationId_C);
        assertThat(stations.stream()
                           .map(StationResponse::getName)
                           .collect(Collectors.toList())).contains(stationName_A, stationName_B, stationName_C);

    }

    /**
     * Given 상행종점역이 A이고, 하행종점역이 C인 지하철 노선 A-B-C를 생성하고
     * When 지하철역 C와 D를 연결하는 구간을 생성하면
     * Then 해당 노선이 A-B-C-D로 연결되고,
     * Then 지하철 노선의 하행역이 D로 바뀐다.
     */
    @DisplayName("3개역이 있는 노선에 지하철 구간을 추가한다.")
    @Test
    void createSection_성공__3개역_노선() {

        // given
        Long lineId = createLine(stationId_A, stationId_B);
        createSection(lineId, stationId_B, stationId_C);

        // when
        ExtractableResponse<Response> creationResponse = createSection(lineId, stationId_C, stationId_D);

        // then
        assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<StationResponse> stations = LineApiHelper.callApiToGetSingleLine(lineId)
                                                      .jsonPath()
                                                      .getList("stations", StationResponse.class);
        assertThat(stations.size()).isEqualTo(4);
        assertThat(stations.stream()
                           .map(StationResponse::getId)
                           .collect(Collectors.toList())).contains(stationId_A, stationId_B, stationId_C, stationId_D);
        assertThat(stations.stream()
                           .map(StationResponse::getName)
                           .collect(Collectors.toList())).contains(stationName_A, stationName_B, stationName_C, stationName_D);
    }

    /**
     * Given 상행종점역이 A이고, 하행종점역이 B인 지하철 노선 A-B를 생성하고
     * When 지하철역 C와 하행종점역이 아닌 지하철역 A를 연결하는 구간을 생성하면
     * Then 에러를 응답한다.
     */
    @DisplayName("[오류] 노선의 하행 종점역이 아닌 지하철역을 상행역으로 하는 구간을 추가한다.")
    @Test
    void createSection_에러__하행종점역이_아닌_상행역_구간() {

        // given
        Long lineId = createLine(stationId_A, stationId_B);

        // when
        ExtractableResponse<Response> creationResponse = createSection(lineId, stationId_C, stationId_A);

        // then
        assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    /**
     * Given 상행종점역이 A이고, 하행종점역이 C인 지하철 노선 A-B-C를 생성하고
     * When 지하철역 C와 이미 노선에 존재하는 B를 연결하는 구간을 생성하면
     * Then 에러를 응답한다.
     */
    @DisplayName("[오류] 이미 노선에 등록된 지하철역을 하행역으로 하는 지하철 구간을 추가한다.")
    @Test
    void createSection_에러__노선에_등록된_하행역() {

        // given
        Long lineId = createLine(stationId_A, stationId_B);
        createSection(lineId, stationId_B, stationId_C);

        // when
        ExtractableResponse<Response> creationResponse = createSection(lineId, stationId_C, stationId_B);

        // then
        assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> createSection(Long lineId, Long upStationId, Long downStationId) {
        return callApiToCreateSection(lineId, SectionSaveRequest.builder()
                                                                .upStationId(upStationId)
                                                                .downStationId(downStationId)
                                                                .distance(3L)
                                                                .build());
    }

    private static Long createLine(Long upStationId, Long downStationId) {
        return LineApiHelper.callApiToCreateLine(LineSaveRequest.builder()
                                                                .name("테스트 노선")
                                                                .color("bg-red-600")
                                                                .upStationId(upStationId)
                                                                .downStationId(downStationId)
                                                                .distance(10L)
                                                                .build())
                            .jsonPath()
                            .getLong("id");
    }

}