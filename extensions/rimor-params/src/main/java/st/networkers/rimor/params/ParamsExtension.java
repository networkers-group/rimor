package st.networkers.rimor.params;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.extension.RimorExtension;
import st.networkers.rimor.params.parse.support.*;

public class ParamsExtension implements RimorExtension {

    @Override
    public void configure(Rimor rimor) {
        rimor.registerExecutionContextProvider(new BooleanInstructionParamParser())
                .registerExecutionContextProvider(new DefaultInstructionParamParser())
                .registerExecutionContextProvider(new EnumInstructionParamParser())
                .registerExecutionContextProvider(new StringInstructionParamParser())
                .registerExecutionContextProvider(new TextInstructionParamParser());
    }
}
