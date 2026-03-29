package service;

import dto.UserRegistrationDto;
import entity.User;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerNewUser(UserRegistrationDto dto) {
        // 1. Перевірка унікальності
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        // 2. Створення об'єкта User з DTO
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // Поки що без хешування

        return userRepository.save(user);
    }
}