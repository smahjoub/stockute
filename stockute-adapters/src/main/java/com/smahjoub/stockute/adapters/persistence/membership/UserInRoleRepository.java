package com.smahjoub.stockute.adapters.persistence.membership;

import com.smahjoub.stockute.domain.model.UserInRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInRoleRepository extends ReactiveCrudRepository<UserInRole, Long> {
}