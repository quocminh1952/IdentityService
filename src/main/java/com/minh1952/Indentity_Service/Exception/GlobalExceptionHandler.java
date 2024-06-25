package com.minh1952.Indentity_Service.Exception;

import com.minh1952.Indentity_Service.dto.request.ApiResponse;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Khai báo nơi xử lý Exception khi có Exception xảy ra
public class GlobalExceptionHandler {


    @ExceptionHandler(value = AppException.class) //tập trung các Exception thuộc loại AppExeption
    // định dạng trả về theo chuẩn json :  ApiResponse
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){

        ErrorCode errorCode = exception.getErrorCode();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }


    //Tập trung các Exception mà Dev quản lý
    @ExceptionHandler(value = Exception.class)
        // định dạng trả về theo chuẩn json: ApiResponse
        ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNKNOWN_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNKNOWN_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Xử lý ngoại lệ MethodArgumentNotValidException
    // thường được ném ra khi có lỗi xác thực (validation) trong các tham số của phương thức (ví dụ: trong các DTO được gửi từ client).
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){

        // lấy mã lỗi từ message trong annoation Size
        String enumKey = exception.getFieldError().getDefaultMessage();


        // chuyển đổi mã thông báo enumkey trong message thành ErrorCode đã được định nghĩa
        // cho errorCode giá trị là INVALID_KEY tránh trường hợp giá trị enumKey không được tìm thấy trong ErrorCode / dùng sai message key trong @Size
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try{
            errorCode = ErrorCode.valueOf(enumKey);
        }catch(Exception e){

        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

}
