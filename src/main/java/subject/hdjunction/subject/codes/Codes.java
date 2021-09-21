package subject.hdjunction.subject.codes;

public enum Codes {
    S0000("S0090", "정상"),
    D2000("D2000", "삭제"),
    E4000("E4000", "오류");

    public final String code;

    public final String desc;

    Codes(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
