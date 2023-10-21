package com.example.convention.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.convention.entity.Comment;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Transactional
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findAll() {
        // given
        Comment comment = new Comment("example");
        Comment savedComment = commentRepository.save(comment);

        // when
        List<Comment> comments = commentRepository.findAll();

        // then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0)).isEqualTo(savedComment);
    }

    @Test
    void findById() {
        // given
        Comment comment = new Comment("example");
        Comment savedComment = commentRepository.save(comment);

        // when
        Comment foundComment = commentRepository.findById(savedComment.getId()).orElseThrow();

        // then
        assertThat(foundComment).isEqualTo(savedComment);
    }

    @Test
    void deleteById() {
        // given
        Comment comment = new Comment("example");
        Comment savedComment = commentRepository.save(comment);

        // when
        commentRepository.deleteById(savedComment.getId());

        // then
        assertThat(commentRepository.count()).isEqualTo(0);
    }

    @Test
    void save() {
        // given
        Comment comment = new Comment("example");

        // when
        commentRepository.save(comment);

        // then
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getComment()).isEqualTo("example");
    }
}
