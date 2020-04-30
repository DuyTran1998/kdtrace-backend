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

        Role roleAdmin = new Role(RoleName.ROLE_ADMIN);
        Role roleUser = new Role(RoleName.ROLE_USER);
        Role roleTransport = new Role(RoleName.ROLE_PRODUCER);
        Role roleExpress = new Role(RoleName.ROLE_TRANSPORT);
        Role roleDistributor = new Role(RoleName.ROLE_DISTRIBUTOR);

        userRepository.save( new User("duytran", passwordEncoder.encode("123456"), true, roleAdmin));

        roleRepository.save(roleUser);
        roleRepository.save(roleTransport);
        roleRepository.save(roleExpress);
        roleRepository.save(roleDistributor);

    }
}