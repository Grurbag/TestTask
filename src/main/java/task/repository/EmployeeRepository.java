package task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import task.entity.Employee;

/*
 * Задание 1.
 */

@Repository
public
interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}