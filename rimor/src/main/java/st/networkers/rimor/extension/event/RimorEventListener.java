package st.networkers.rimor.extension.event;

public interface RimorEventListener<E extends RimorEvent> {

    void onEvent(E event);

}
