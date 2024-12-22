package com.hust.smart_Shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.constants.KeyId.UserFamilyKey;
import com.hust.smart_Shopping.models.UserFamily;
import com.hust.smart_Shopping.models.Family;
import com.hust.smart_Shopping.models.User;
import java.util.List;
import java.util.Optional;

public interface UserFamilyRepository extends JpaRepository<UserFamily, UserFamilyKey> {
    boolean existsByUser(User user);

    Optional<UserFamily> findByUser(User user);

    List<UserFamily> findByFamily(Family family);
}
