package com.minh1952.Indentity_Service.dto.request;


//Tạo 1 chuẩn Api trả về :

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) //định dạng json : nếu có trường = null thì loại ra khỏi response json
public class ApiResponse<T> {
    private int code =1000; //mặc định nếu request thành công thì reponse code =1000
    private String message;
    private T result;

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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}
