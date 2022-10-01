package st.networkers.rimor.internal.provide.builtin;

import st.networkers.rimor.provide.ProvidesParameter;
import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.provide.RimorProviderWrapper;
import st.networkers.rimor.provide.builtin.Param;
import st.networkers.rimor.provide.builtin.Params;

import java.util.List;

public class ExecutionParametersProvider implements RimorProviderWrapper {

    @ProvidesParameter
    @RequireAnnotations(Param.class)
    public String provide(Param param, @Params List<String> commandParameters) {
        int position = param.value();

        return position < commandParameters.size() ? commandParameters.get(position) : null;
    }
}
