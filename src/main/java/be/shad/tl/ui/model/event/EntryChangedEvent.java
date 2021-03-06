package be.shad.tl.ui.model.event;

public class EntryChangedEvent implements Event {
    private final String entryId;

    public EntryChangedEvent(String entryId) {
        this.entryId = entryId;
    }

    public String getEntryId() {
        return entryId;
    }
}
