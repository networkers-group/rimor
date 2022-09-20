package st.networkers.rimor.internal.builtin;

import st.networkers.rimor.provide.RequireAnnotations;
import st.networkers.rimor.command.parameter.Param;
import st.networkers.rimor.command.parameter.Params;
import st.networkers.rimor.provide.ProvidesParameter;

import java.util.List;

public class ParamProvider {

    @ProvidesParameter
    @RequireAnnotations(Param.class)
    public String provide(Param param, @Params List<String> commandParameters) {
        int position = param.value();

        return position < commandParameters.size() ? commandParameters.get(position) : null;
    }
}
