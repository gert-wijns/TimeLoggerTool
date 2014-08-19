package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.DATE_FORMAT;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class Parameters {
    private final String id;
    private final int revision;
    private PMap<String, String> parameters;

    public Parameters(String id, int revision) {
        this.id = id;
        this.revision = revision;
        this.parameters = HashTreePMap.empty();
    }

    public String getId() {
        return id;
    }

    public int getRevision() {
        return revision;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Date getDate(String key) {
        String value = parameters.get(key);
        if (value == null) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String key) {
        return parameters.get(key);
    }

    public Parameters put(String key, Date date) {
        if (date != null) {
            parameters = parameters.plus(key, DATE_FORMAT.format(date));
        }
        return this;
    }

    public Parameters put(String key, String value) {
        if (value != null) {
            parameters = parameters.plus(key, value);
        }
        return this;
    }

    @Override
    public String toString() {
        return id;
    }
}
