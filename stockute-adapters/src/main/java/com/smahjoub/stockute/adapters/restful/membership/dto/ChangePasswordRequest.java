package com.smahjoub.stockute.adapters.restful.membership.dto;

public record ChangePasswordRequest(String username, String oldPassword, String newPassword, String newPasswordConfirm) {
}
