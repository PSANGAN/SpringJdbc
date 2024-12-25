package com.pcgs.spring.Jdbc.dao;

import com.pcgs.spring.Jdbc.model.Course;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CourseJdbcDAO implements DAO<Course> {

    private JdbcTemplate jdbcTemplate;

    public CourseJdbcDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Course> list() {
        String sql = "SELECT course_id, title, description, link from course";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Course course = new Course();
            course.setCourseId(rs.getInt("course_id"));
            course.setTitle(rs.getString("title"));
            course.setDescription(rs.getString("description"));
            course.setLink(rs.getString("link"));
            return course;
        });
    }

    @Override
    public void create(Course course) {
        String sql = "insert into course(course_id,title,description,link) values(?,?,?,?)";
        int insert = jdbcTemplate.update(sql,7,course.getTitle(),course.getDescription(),course.getLink());
        if(insert == 1) {
            System.out.println("New Course Created: " + course.getTitle());
        }
    }

    @Override
    public Optional<Course> get(int id) {
        String sql = "SELECT course_id,title,description,link from course where course_id = ?";
        Course course = null;
        try {
            Course finalCourse = course;
            course = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                finalCourse.setCourseId(rs.getInt("course_id"));
                finalCourse.setTitle(rs.getString("title"));
                finalCourse.setDescription(rs.getString("description"));
                finalCourse.setLink(rs.getString("link"));
                return finalCourse;
            });
        }catch (DataAccessException ex) {
            System.out.println("Course not found: " + id);
        }
        return Optional.ofNullable(course);
    }

    @Override
    public void update(Course course, int id) {
        String sql = "update course set title = ?, description = ?, link = ? where course_id = ?";
        int update = jdbcTemplate.update(sql,course.getTitle(),course.getDescription(),course.getLink(),id);
        if(update == 1) {
            System.out.println("Course Updated: " + course.getTitle());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "delete from course where course_id = ?";
        int delete = jdbcTemplate.update(sql,id);
        if(delete == 1) {
            System.out.println("Course Deleted: " + id);
        }
    }
}
