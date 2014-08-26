package be.shad.tl.util;

import javafx.beans.property.ObjectProperty;

public class BufferedProperty<V, T extends ObjectProperty<V>> {
    private T original;
    private T actual;

    public BufferedProperty(T original, T actual) {
        this.original = original;
        this.actual = actual;
    }

    public T getOriginal() {
        return original;
    }
    
    public T getActual() {
        return actual;
    }
    
    public void commit() {
        original.set(actual.get());
    }
    
    public void revert() {
        actual.set(original.get());
    }
    
    public boolean isDirty() {
        Object actual = this.actual.get();
        Object original = this.original.get();
        if (actual == null || original == null) {
            return actual != original;
        } else {
            return !actual.equals(original);
        }
    }
}
