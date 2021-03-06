package Dao;

import models.Schools;
import models.Students;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oStudentsDao implements StudentsDao{

    private final Sql2o sql2o;

    public Sql2oStudentsDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Students students) {

        String sql = "INSERT INTO students (studentname, studentemail, studentcourse) VALUES (:studentname, :studentemail, :studentcourse)";
        try(Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(students)
                    .executeUpdate()
                    .getKey();
            students.setId(id);
        } catch (Sql2oException error){
            System.out.println(error);
        }
    }


    @Override
    public void addStudentsToSchool(Students students, Schools schools) {
        String sql = "INSERT INTO school_student (studentid, schoolid) VALUES (:studentid, :schoolid)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("studentid", students.getId())
                    .addParameter("schoolid", schools.getId())
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }

    }

    @Override
    public List<Students> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM students")
                    .executeAndFetch(Students.class);

        }
    }



    @Override
    public Students findById(int Id) {
        String sql = "SELECT * FROM students WHERE id = :id";
        try(Connection conn = sql2o.open()){
            return conn.createQuery(sql)
                    .addParameter("id", Id)
                    .executeAndFetchFirst(Students.class);
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM students";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
