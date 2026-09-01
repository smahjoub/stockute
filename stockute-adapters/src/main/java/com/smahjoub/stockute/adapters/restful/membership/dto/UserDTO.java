package com.smahjoub.stockute.adapters.restful.membership.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserDTO(Long userId,
                      String userName,
                      String email,
                      String firstName,
                      String lastName,
                      Boolean active,
                      List<String> roles,
                      LocalDateTime createdDate,
                      LocalDateTime lastModifiedDate,
                      Long version) {
}