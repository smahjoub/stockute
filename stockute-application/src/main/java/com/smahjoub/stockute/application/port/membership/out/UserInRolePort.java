package com.smahjoub.stockute.application.port.membership.out;

import com.smahjoub.stockute.domain.model.Role;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserInRolePort {

    Mono<List<Role>> findRolesByUserName(String userName);
}
