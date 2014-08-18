package be.shad.tl.service.parser;

public class StringParam {
    private final String key;
    private final String value;

    public StringParam(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
