package task.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import task.entity.Employee;
import task.repository.EmployeeRepository;
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class CreateDatabase {

    private static final Logger logger = LoggerFactory.getLogger(CreateDatabase.class);
    public static String pattern = "dd-MM-yyyy";
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        List<String> randomNames = Arrays.asList("Ivan", "Boris", "Andrew", "Egor", "Alexandr", "Vladimir", "Sergey", "Petr");
        List<String> randomSurnames = Arrays.asList("Timoshinov", "Petrov", "Ivanov", "Smirnov", "Ivannikov", "Volchenko", "Artemiev", "Aslanov", "Alexeev");
        List<String> randomDepartment = Arrays.asList("development", "analytics", "testing");
        Random random = new Random();
        random.setSeed(42);
        AtomicInteger id = new AtomicInteger();
        return args -> {
            for (int i = 0; i < 100; i++) {
                logger.info("Preloading " + repository.save(new Employee(id.getAndIncrement(), randomNames.get(random.nextInt(randomNames.size())), randomSurnames.get(random.nextInt(randomSurnames.size())), LocalDate.parse(generateRandomDate(random), dateTimeFormatter), randomDepartment.get(random.nextInt(randomDepartment.size())), ThreadLocalRandom.current().nextInt(25000, 150000))));
            }
        };
    }

    private String generateRandomDate(Random random) {
        int day = ThreadLocalRandom.current().nextInt(1, 30);
        int month = ThreadLocalRandom.current().nextInt(1, 12);
        return (day < 10 ? "0" + day : day) + "-" + (month < 10 ? "0" + month : month) + "-" + (1990 + random.nextInt( 12));
    }
}