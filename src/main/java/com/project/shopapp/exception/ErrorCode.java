package com.project.shopapp.exception;

import com.project.shopapp.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "uncategorized error",HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_KEY(1001,"Invalid key", HttpStatus.BAD_REQUEST),

    CATEGORY_NAME_INVALID(1008, "Category's name cannot be empty", HttpStatus.BAD_REQUEST),
    BRAND_NAME_INVALID(1035, "Category's name cannot be empty", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(1009, "Category exitsted", HttpStatus.BAD_REQUEST),
    BRAND_EXISTED(1036, "Category exitsted", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1010, "Category not found", HttpStatus.NOT_FOUND),
    BRAND_NOT_EXISTED(1010, "Category not found", HttpStatus.NOT_FOUND),

    PRODUCT_NAME_INVALID(1020, "Product name is required", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_INVALID(1021, "Product price must be >= 0", HttpStatus.BAD_REQUEST),
    PRODUCT_EXISTED(1011, "Product exitsted", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1012, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_IMAGE_INVALID(1013, "Number of images lest " +
            ProductImage.MAXIMUM_IMAGES_PER_PRODUCT + " reached", HttpStatus.BAD_REQUEST),

    FILES_IMAGES_SIZE_FAILED(1014, "File image too large! Maximum size is 10MB ", HttpStatus.PAYLOAD_TOO_LARGE),
    FILES_IMAGES_TYPE_FAILED(1015, "File must be an image ", HttpStatus.UNSUPPORTED_MEDIA_TYPE),

    PRODUCT_SIZE_NOT_EXIST(1026, "Product size not found", HttpStatus.NOT_FOUND),
    PRODUCT_COLOR_NOT_EXIST(1026, "Product color not found", HttpStatus.NOT_FOUND),
    //order
    USER_ID_INVALID(1016, "User's ID must be > 0", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_IS_NULL(1017, "Phone number is required", HttpStatus.BAD_REQUEST),
    TOTAL_MONEY_INVALID(1018, "Total money must be >= 0", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_SIZE_INVALID(1019, "Phone number size is 10", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTED(1024, "Đơn hàng không tồn tại", HttpStatus.NOT_FOUND),

    SHIPPING_DATE(1022, "Ngày giao hàng ko hợp lệ", HttpStatus.BAD_REQUEST),
    SHIPPING_ADDRESS_IS_NULL(1023, "Shipping address is required", HttpStatus.BAD_REQUEST),

    //order_detail
    ORDER_DETAIL_NOT_EXISTED(1025, "Chi tiết đơn hàng trống", HttpStatus.NOT_FOUND),


    CART_NOT_EXIST(1025, "Giỏ hàng trống", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(1025, "Sản phẩm không tồn tại", HttpStatus.NOT_FOUND),
    INSUFFICIENT_QUANTITY(1025, "Số lượng sản phẩm không đủ", HttpStatus.BAD_REQUEST),

    EMAIL_REQUIRED(1031, "Email is required", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User exitsted", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    PASSWORD_WRONG(1030, "Mật khẩu mới không trùng khớp", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_WRONG(1034, "Sai mật khẩu", HttpStatus.BAD_REQUEST),
    NAME_REQUIRED(1032, "Vui lòng nhập tên", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1033, "Please enter a valid email, containing '@' and a domain (e.g., user@example.com).", HttpStatus.BAD_REQUEST),
            ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
