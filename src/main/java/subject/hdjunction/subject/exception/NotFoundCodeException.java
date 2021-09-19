package subject.hdjunction.subject.exception;

public class NotFoundCodeException extends RuntimeException{
    public NotFoundCodeException() {
        super("코드를 찾을 수 없습니다");
    }
}
