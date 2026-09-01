package com.smahjoub.stockute.adapters.restful.membership.dto;

public record CreateUserRequest(String email,
                                String username,
                                String password,
                                String firstName,
                                String lastName,
                                String taxResidencyCountry) {
}