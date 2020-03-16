package org.basecampcodingacademy.reflections.db;

import org.basecampcodingacademy.reflections.domain.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class QuestionRepository {
    @Autowired
    public JdbcTemplate jdbc;

    public List<Question> all() {
        return jdbc.query("SELECT id, prompt FROM questions", this::mapper);
    }

    public Question create(Question question) {
        return jdbc.queryForObject(
                "INSERT INTO questions (prompt) VALUES (?) RETURNING id, prompt",
                this::mapper,
                question.prompt
        );
    }

//    public Question find() {
//        try {
//            return jdbc.queryForObject("SELECT id, prompt FROM questions WHERE prompt = ? LIMIT 1", this::mapper);
//        } catch (EmptyResultDataAccessException ex) {
//            return null;
//        }
//    }

    public Question find(Integer id) {
        return jdbc.queryForObject("SELECT id, prompt FROM questions WHERE id = ?", this::mapper, id);
    }

    public Question update(Question question) {
        return jdbc.queryForObject(
                "UPDATE questions SET prompt = ? WHERE id = ? RETURNING id, prompt",
                this::mapper, question.prompt, question.id);
    }

    public void delete(Integer id) {
        jdbc.query("DELETE FROM questions WHERE id = ? RETURNING id, prompt", this::mapper, id);
    }

    private Question mapper(ResultSet resultSet, int i) throws SQLException {
        return new Question(
                resultSet.getInt("id"),
                resultSet.getString("prompt"),
                resultSet.getInt("reflectionId")
                );
    }
}