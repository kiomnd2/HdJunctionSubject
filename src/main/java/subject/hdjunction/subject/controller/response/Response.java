package subject.hdjunction.subject.controller.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subject.hdjunction.subject.codes.Codes;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Response<T> {

    private final String code;

    private final T body;

    public static <T> Response<T> success(T body) {
        return new Response<>(Codes.S0000.code, body);
    }

    public static <T> Response<T> fail(T body) {
        return new Response<>(Codes.E4000.code, body);
    }

}
