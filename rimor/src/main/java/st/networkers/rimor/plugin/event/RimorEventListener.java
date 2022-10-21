package st.networkers.rimor.plugin.event;

public interface RimorEventListener<E extends RimorEvent> {

    void onEvent(E event);

}
