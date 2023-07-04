package st.networkers.rimor.params;

import st.networkers.rimor.extension.AbstractRimorExtension;
import st.networkers.rimor.params.parse.builtin.BooleanInstructionParamParser;
import st.networkers.rimor.params.parse.builtin.DefaultInstructionParamParser;
import st.networkers.rimor.params.parse.builtin.EnumInstructionParamParser;
import st.networkers.rimor.params.parse.builtin.StringInstructionParamParser;

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
