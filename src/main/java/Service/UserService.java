package Service;

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
    public User registerNewUser(User user) {
        // 1. Перевірка унікальності (вимоги 2.1.2) [cite: 24, 25]
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        // 2. Тут пізніше додамо хешування пароля (вимога 2.1.4) [cite: 37]

        return userRepository.save(user);
    }
}