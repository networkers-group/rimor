package st.networkers.rimor.params;

import st.networkers.rimor.extension.AbstractRimorExtension;
import st.networkers.rimor.params.parse.support.BooleanInstructionParamParser;
import st.networkers.rimor.params.parse.support.DefaultInstructionParamParser;
import st.networkers.rimor.params.parse.support.EnumInstructionParamParser;
import st.networkers.rimor.params.parse.support.StringInstructionParamParser;

public class ParamsExtension extends AbstractRimorExtension {

    @Override
    public void configure() {
        registerProvider(new BooleanInstructionParamParser());
        registerProvider(new DefaultInstructionParamParser());
        registerProvider(new EnumInstructionParamParser());
        registerProvider(new StringInstructionParamParser());
    }

    @Override
    public void initialize() {
    }
}
