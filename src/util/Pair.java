package util;

public class Pair<E, F> {

    E key;
    F value;

    public Pair(E key, F value) {
        this.key = key;
        this.value = value;
    }

    public E getKey() {
        return key;
    }

    public void setKey(E key) {
        this.key = key;
    }

    public F getValue() {
        return value;
    }

    public void setValue(F value) {
        this.value = value;
    }
}
