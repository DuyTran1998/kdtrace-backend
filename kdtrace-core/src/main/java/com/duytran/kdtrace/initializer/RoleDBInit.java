package com.duytran.kdtrace.initializer;

import com.duytran.kdtrace.entity.Role;
import com.duytran.kdtrace.entity.RoleName;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.repository.RoleRepository;
import com.duytran.kdtrace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@ConditionalOnProperty(name = "kdtrace.db-init", havingValue = "true")
public class RoleDBInit implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        this.roleRepository.deleteAll();

        Role roleAdmin = new Role(1, RoleName.ROLE_ADMIN);
        Role roleUser = new Role(2, RoleName.ROLE_USER);
        Role roleExpress = new Role(3, RoleName.ROLE_EXPRESS);
        Role roleProducer = new Role(4, RoleName.ROLE_PRODUCER);
        Role roleDistributor = new Role(5, RoleName.ROLE_DISTRIBUTOR);

        userRepository.save( new User("duytran", passwordEncoder.encode("123456"), true, roleAdmin));

        roleRepository.save(roleUser);
        roleRepository.save(roleExpress);
        roleRepository.save(roleProducer);
        roleRepository.save(roleDistributor);



    }
}