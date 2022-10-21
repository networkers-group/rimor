package st.networkers.rimor.params;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.params.parse.builtin.BooleanParamParser;
import st.networkers.rimor.params.parse.builtin.EnumParamParser;
import st.networkers.rimor.params.parse.builtin.StringParamParser;
import st.networkers.rimor.plugin.AbstractRimorPlugin;

public class ParamsPlugin extends AbstractRimorPlugin {

    @Override
    public void configure(Rimor rimor) {
        registerProvider(new BooleanParamParser());
        registerProvider(new EnumParamParser());
        registerProvider(new StringParamParser());
    }
}
