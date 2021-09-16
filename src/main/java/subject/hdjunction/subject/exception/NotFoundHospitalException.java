package subject.hdjunction.subject.exception;

public class NotFoundHospitalException extends RuntimeException{
    public NotFoundHospitalException() {
        super("병원을 찾을 수 없습니다");
    }
}
