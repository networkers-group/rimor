package st.networkers.rimor.extension.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import st.networkers.rimor.Rimor;

@Getter
@RequiredArgsConstructor
public class RimorInitializationEvent implements RimorEvent {
    private final Rimor rimor;
}
