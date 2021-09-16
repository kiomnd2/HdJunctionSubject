package subject.hdjunction.subject.codes;

public enum Codes {
    S0000("000", "정상"),
    D2000("200", "삭제"),
    E4000("400", "오류");

    public final String code;

    public final String desc;

    Codes(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
