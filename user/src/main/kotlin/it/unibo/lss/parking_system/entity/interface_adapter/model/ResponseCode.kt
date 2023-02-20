package it.unibo.lss.parking_system.entity.interface_adapter.model

enum class ResponseCode(val code: String) {
    SUCCESS("success"),
    UNAUTHORIZED("unauthorizedUser"),
    USER_NOT_FOUND("userNotFoundError"),
    PASSWORD_ERROR("passwordError"),
    ALREADY_REGISTERED("alreadyRegistered")
}
