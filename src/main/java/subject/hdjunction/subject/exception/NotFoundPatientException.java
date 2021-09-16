package subject.hdjunction.subject.exception;

public class NotFoundPatientException extends RuntimeException{
    public NotFoundPatientException() {
        super("환자를 찾을 수 없습니다");
    }
}
