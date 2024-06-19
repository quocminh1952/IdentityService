package com.minh1952.Indentity_Service.Repository;

import com.minh1952.Indentity_Service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository                              // JpaRepository<@Entity,@Id>
public interface userRepository extends JpaRepository<User,String> {
    boolean existsByUsername(String username);
}
