package st.networkers.rimor.params;

import st.networkers.rimor.extension.AbstractRimorExtension;
import st.networkers.rimor.params.parse.builtin.BooleanParamParser;
import st.networkers.rimor.params.parse.builtin.EnumParamParser;
import st.networkers.rimor.params.parse.builtin.PresentObjectParamParser;

public class ParamsExtension extends AbstractRimorExtension {

    @Override
    public void configure() {
        registerProvider(new BooleanParamParser());
        registerProvider(new EnumParamParser());
        registerProvider(new PresentObjectParamParser());
    }

    @Override
    public void initialize() {
    }
}
