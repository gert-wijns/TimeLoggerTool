package be.shad.tl.service.parser;

import java.util.Map;

import be.shad.tl.service.TimeLogger;

public interface Parser {

    void apply(TimeLogger timeLogger, int revision, Map<String, String> arguments);

}
