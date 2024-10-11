package task.service;

import task.entity.Employee;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EmployeeService {

    private Connection connection;

    public EmployeeService(String url, String user, String password) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, password);
        if (con != null) {
            this.connection = con;
            System.out.println("Установлено соединение\n");
        } else {
            System.out.println("Ошибка при установке соединения\n");
        }
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public ResultSet executeQuery(String request) throws SQLException {
        return connection.createStatement().executeQuery(request);
    }

    public Employee findById(int id) {
        try {
            ResultSet result = executeQuery("SELECT * FROM sys.employee where id=" + id);
            if (result.next())
                return new Employee(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<String> groupByName() {
        try {
            ResultSet result = executeQuery("SELECT name FROM sys.employee group by name");
            List<String> names = new ArrayList<>();
            while (result.next()) {
                names.add(result.getString("name"));
            }
            return names;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> findBetween(LocalDate min, LocalDate max) {
        try {
            ResultSet result = executeQuery(String.format("SELECT * FROM sys.employee where (birth_date >='%s') and (birth_date <='%s')", min, max));
            List<Employee> employees = new ArrayList<>();
            while (result.next()) {
                employees.add(new Employee(result));
            }
            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}