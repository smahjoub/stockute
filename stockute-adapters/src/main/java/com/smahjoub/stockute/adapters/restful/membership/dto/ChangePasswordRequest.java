package com.smahjoub.stockute.adapters.restful.membership.dto;

public record ChangePasswordRequest(String currentPassword, String newPassword) {
}
