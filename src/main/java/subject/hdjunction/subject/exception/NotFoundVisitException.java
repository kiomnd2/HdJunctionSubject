package subject.hdjunction.subject.exception;

public class NotFoundVisitException extends RuntimeException{
    public NotFoundVisitException() {
        super("환자 접수 기록을 찾을 수 없습니다");
    }
}
