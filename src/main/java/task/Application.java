package task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import task.config.CreateDatabase;
import task.entity.Employee;
import task.service.EmployeeService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        try {
            EmployeeService employeeService = new EmployeeService("jdbc:mysql://localhost:3306/sys", "root", "root");
            executeActions(employeeService);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void executeActions(EmployeeService employeeService) throws SQLException {
        Scanner scanner =  new Scanner(System.in);
        boolean stop = false;
        while (true) {
            System.out.println("""
                    Доступные действия:
                    Найти сотрудника по ID - 1
                    Сгруппировать сотрудников по именам - 2
                    Поиск между датами - 3
                    Выйти - 4
                    Введите номер действия:
                    """);
            String actionNumber = scanner.nextLine();
            switch (actionNumber) {
                case ("1") -> {
                    int id = 0;
                    System.out.println("Ввведите id искомого сотрудника: \n");
                    try {
                        id = Integer.parseInt(scanner.nextLine());
                        if (id < 1) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        System.out.println("Нужно ввести число > 1!");
                    }
                    Employee employee = employeeService.findById(id);
                    System.out.println(employee == null ? "Сотрудника с таким id не существует!" : employee);
                }
                case ("2") -> {
                    List<String> employees = employeeService.groupByName();
                    for (String employeeName : employees) {
                        System.out.println(employeeName + "\n");
                    }
                }
                case ("3") -> {
                    try {
                        System.out.println("Введите дату начала поиска(" + CreateDatabase.pattern + "): \n");
                        LocalDate minDate = LocalDate.parse(scanner.nextLine(), CreateDatabase.dateTimeFormatter);
                        System.out.println("Введите дату конца поиска(" + CreateDatabase.pattern + "): \n");
                        LocalDate maxDate = LocalDate.parse(scanner.nextLine(), CreateDatabase.dateTimeFormatter);
                        List<Employee> employees = employeeService.findBetween(minDate, maxDate);
                        for (Employee employee : employees) {
                            System.out.println(employee.toString() + "\n");
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println("Некорректно введена дата");
                    }
                }
                case ("4") -> {
                    employeeService.closeConnection();
                    stop = true;
                }
            }
            if (stop) {
                break;
            }
        }
    }
}