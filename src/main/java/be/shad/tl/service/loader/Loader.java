package be.shad.tl.service.loader;

import be.shad.tl.service.TimeLogger;

public interface Loader {

    String getId();

    void apply(TimeLogger timeLogger, Parameters params);

}
