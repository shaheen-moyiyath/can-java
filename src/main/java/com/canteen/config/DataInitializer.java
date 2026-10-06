package com.canteen.config;

import com.canteen.entity.MenuItem;
import com.canteen.entity.Role;
import com.canteen.entity.User;
import com.canteen.repository.MenuItemRepository;
import com.canteen.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           MenuItemRepository menuItemRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedMenuItems();
    }

    private void seedUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "Canteen Administrator",
                    "admin@canteen.college.edu",
                    Role.ADMIN
            );
            userRepository.save(admin);
            log.info("Initialized default ADMIN user: admin / admin123");
        } else {
            // Ensure password hash is valid
            userRepository.findByUsername("admin").ifPresent(admin -> {
                if (!passwordEncoder.matches("admin123", admin.getPasswordHash())) {
                    admin.setPasswordHash(passwordEncoder.encode("admin123"));
                    userRepository.save(admin);
                }
            });
        }

        if (!userRepository.existsByUsername("student1")) {
            User student = new User(
                    "student1",
                    passwordEncoder.encode("student123"),
                    "Muhammed Shaheen",
                    "shaheen@student.college.edu",
                    Role.STUDENT
            );
            userRepository.save(student);
            log.info("Initialized default STUDENT user: student1 / student123");
        } else {
            userRepository.findByUsername("student1").ifPresent(student -> {
                if (!passwordEncoder.matches("student123", student.getPasswordHash())) {
                    student.setPasswordHash(passwordEncoder.encode("student123"));
                    userRepository.save(student);
                }
            });
        }

        if (!userRepository.existsByUsername("AZAYSCS043")) {
            User student2 = new User(
                    "AZAYSCS043",
                    passwordEncoder.encode("student123"),
                    "Sainul Ashiqu N",
                    "sainul@student.college.edu",
                    Role.STUDENT
            );
            userRepository.save(student2);
            log.info("Initialized default STUDENT user: AZAYSCS043 / student123");
        }
    }

    private void seedMenuItems() {
        if (menuItemRepository.count() == 0) {
            List<MenuItem> defaultItems = List.of(
                    new MenuItem("Chicken Biryani", "Fragrant Basmati rice cooked with tender spiced chicken, egg, and raita", new BigDecimal("120.00"), "Biryani", true),
                    new MenuItem("Veg Thali / Kerala Meals", "Traditional vegetarian meal with rice, sambar, thoran, rasam, and pickle", new BigDecimal("60.00"), "Meals", true),
                    new MenuItem("Fish Curry Meals", "Kerala rice meal served with hot spicy sardine/mackerel fish curry", new BigDecimal("90.00"), "Meals", true),
                    new MenuItem("Masala Dosa", "Crispy fermented crepe stuffed with savory mashed spiced potatoes", new BigDecimal("50.00"), "Breakfast", true),
                    new MenuItem("Egg Puffs", "Flaky golden baked pastry filled with spiced egg filling", new BigDecimal("20.00"), "Snacks", true),
                    new MenuItem("Chicken Roll", "Shredded chicken sauteed in onion and peppers rolled in flatbread", new BigDecimal("60.00"), "Snacks", true),
                    new MenuItem("Tea / Chai", "Freshly brewed hot cardamom tea", new BigDecimal("12.00"), "Beverages", true),
                    new MenuItem("Cold Coffee", "Chilled blended milk coffee with chocolate drizzle", new BigDecimal("35.00"), "Beverages", true)
            );
            menuItemRepository.saveAll(defaultItems);
            log.info("Initialized {} default menu items.", defaultItems.size());
        }
    }
}
