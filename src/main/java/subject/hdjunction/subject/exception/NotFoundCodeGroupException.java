package subject.hdjunction.subject.exception;

public class NotFoundCodeGroupException extends RuntimeException{
    public NotFoundCodeGroupException() {
        super("코드그룹을 찾을 수 없습니다");
    }
}
