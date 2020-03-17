package org.basecampcodingacademy.reflections.db;

import org.basecampcodingacademy.reflections.domain.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Repository
public class ResponseRepository {
    @Autowired
    public JdbcTemplate jdbc;

    public List<Response> all() {
        return jdbc.query("SELECT id, answers FROM reflections", this::mapper);
    }

    public Response create(Response reflection) {
        return jdbc.queryForObject(
                "INSERT INTO reflections (answers) VALUES (?) RETURNING id, reflectionId, username, answers",
                this::mapper,
                reflection.answers
        );
    }

//    public Response find(LocalDate localDate) {
//        try {
//            return jdbc.queryForObject("SELECT id, answers FROM reflections WHERE answers = ? LIMIT 1", this::mapper, localDate);
//        } catch (EmptyResultDataAccessException ex) {
//            return null;
//        }
//    }

    public Response find(Integer id) {
        return jdbc.queryForObject("SELECT id, reflectionId, username, answers FROM reflections WHERE id = ?", this::mapper, id);
    }

    public Response update(Response reflection) {
        return jdbc.queryForObject(
                "UPDATE reflections SET answers = ? WHERE id = ? AND reflectionId = ? AND username = ? RETURNING id, reflectionId, username, answers",
                this::mapper, reflection.answers, reflection.id);
    }

    public void delete(Integer id) {
        jdbc.query("DELETE FROM reflections WHERE id = ? RETURNING id, reflectionId, username, answers", this::mapper, id);
    }

    private Response mapper(ResultSet resultSet, int i) throws SQLException {
        return new Response(
                resultSet.getInt("id"),
                resultSet.getInt("reflectionId"),
                resultSet.getString("answers"),
                Collections.singletonList(resultSet.getString("username"))

        );
    }
}
