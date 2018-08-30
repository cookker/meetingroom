package ms.me.meetingroom.common;


import jdk.nashorn.internal.objects.annotations.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Response<T> {
    private final long timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    private final String code;
    private final String message;
    private final T data;

    public static final String SUCCESS_CODE = "0000";
    private static final String SUCCESS_MESSAGE = "SUCCESS";

    public static final String FAIL_CODE = "9999";
    private static final String FAIL_MESSAGE = "FAIL";

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static Response fail(String message) {
        return new Response<>(FAIL_CODE, message, null);
    }

    @Override
    public String toString() {
        return "Response{" +
                "timestamp=" + timestamp +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
