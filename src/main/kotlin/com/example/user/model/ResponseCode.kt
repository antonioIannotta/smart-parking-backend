package com.example.user.model

enum class ResponseCode(val code: String) {
    SUCCESS("success"),
    UNAUTHORIZED("unauthorizedUser"),
    USER_DELETED("userDeletedError"),
    USER_NOT_FOUND("userNotFoundError"),
    PASSWORD_ERROR("passwordError"),
    ALREADY_REGISTERED("alreadyRegistered")
}
