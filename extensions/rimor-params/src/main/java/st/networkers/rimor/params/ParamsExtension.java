package st.networkers.rimor.params;

import st.networkers.rimor.extension.AbstractRimorExtension;
import st.networkers.rimor.params.parse.builtin.BooleanParamParser;
import st.networkers.rimor.params.parse.builtin.DefaultParamParser;
import st.networkers.rimor.params.parse.builtin.EnumParamParser;
import st.networkers.rimor.params.parse.builtin.StringParamParser;

public class ParamsExtension extends AbstractRimorExtension {

    @Override
    public void configure() {
        registerProvider(new BooleanParamParser());
        registerProvider(new DefaultParamParser());
        registerProvider(new EnumParamParser());
        registerProvider(new StringParamParser());
    }

    @Override
    public void initialize() {
    }
}
