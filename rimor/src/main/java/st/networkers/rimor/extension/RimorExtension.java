package st.networkers.rimor.extension;

import st.networkers.rimor.Rimor;

/**
 * @see AbstractRimorExtension
 */
public interface RimorExtension {

    /**
     * Called when the extension is going to be registered into a {@link Rimor} instance.
     * <p>
     * This is the right place to register the extension commands, providers or execution tasks.
     *
     * @param rimor the {@link Rimor} instance this extension has been registered into.
     */
    void configure(Rimor rimor);

    /**
     * Called inside {@link Rimor#initialize()} for every {@link RimorExtension}.
     * <p>
     * At this point, all the commands, providers and execution tasks of this extension must have been registered.
     */
    void initialize();

}
