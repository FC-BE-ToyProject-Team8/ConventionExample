package com.example.convention.controller;

import static com.example.convention.controller.util.CommentDtoConverter.toCommentResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.example.convention.controller.request.CommentRequest;
import com.example.convention.controller.response.CommentResponse;
import com.example.convention.entity.Comment;
import com.example.convention.repository.CommentRepository;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommentControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 댓글_목록() {
        // given
        String url = "/api/comments";
        Comment givenComment = commentRepository.save(new Comment("example1"));

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when()
                .get(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        List<CommentResponse> data = jsonPath.getList("data", CommentResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data).hasSize(1);
            softly.assertThat(data).contains(toCommentResponse(givenComment));
        });
    }

    @Test
    void 댓글_작성() {
        // given
        String url = "/api/comments";
        CommentRequest request = new CommentRequest("example");

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
            .when()
                .post(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        CommentResponse data = jsonPath.getObject("data", CommentResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.comment()).isEqualTo("example");
        });
        // assertThat은 실패 시 다음 줄로 내려가지 않고 바로 종료되지만, assertSoftly는 실패해도 모든 줄을 실행한다
    }

    @Test
    void 댓글_수정() {
        // given
        Comment givenComment = commentRepository.save(new Comment("before edit"));
        String url = "/api/comments/" + givenComment.getId();

        CommentRequest request = new CommentRequest("after edit");

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
            .when()
                .put(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        CommentResponse data = jsonPath.getObject("data", CommentResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isEqualTo(givenComment.getId());
            softly.assertThat(data.comment()).isEqualTo("after edit");
        });
    }

    @Test
    void 댓글_삭제() {
        // given
        Comment givenComment = commentRepository.save(new Comment("before edit"));
        String url = "/api/comments/" + givenComment.getId();
        
        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when()
                .delete(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(status).isEqualTo("SUCCESS");

        assertThat(commentRepository.findById(givenComment.getId())).isNotPresent();
    }
}
