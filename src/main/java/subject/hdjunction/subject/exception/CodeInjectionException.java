package subject.hdjunction.subject.exception;

public class CodeInjectionException extends RuntimeException{
    public CodeInjectionException() {
        super("코드 생성중 오류가 발생했습니다");
    }
}
