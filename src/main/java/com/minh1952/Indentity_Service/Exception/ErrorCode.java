package com.minh1952.Indentity_Service.Exception;

public enum ErrorCode {

    // Các Exception có thể xảy ra:
    UNKNOWN_EXCEPTION(9999,"ERROR IS UNCATEGORIZED"),
    USER_EXISTED(1001,"User Existed"),
    INVALID_KEY(1000,"Invalid message key"),//trường hợp Spring không tìm lấy ErrorCode mà dev truyền vào ở message trong @Size
    USERNAME_INVALID(1003,"Username must be at least 8 characters"),
    PASSWORD_INVALID(1002,"Password must be at least 8 characters")
    ;



    private int code;
    private String message;

     ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
