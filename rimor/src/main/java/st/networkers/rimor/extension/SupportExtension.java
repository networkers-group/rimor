package st.networkers.rimor.extension;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.bean.BeanManager;
import st.networkers.rimor.command.CommandProcessor;
import st.networkers.rimor.context.provide.support.OptionalProvider;
import st.networkers.rimor.instruction.InstructionResolver;

public class SupportExtension implements RimorExtension {

    private Rimor rimor;

    @Override
    public void configure(Rimor rimor) {
        this.rimor = rimor;

        registerSupportBeanProcessors();
        registerSupportProviders();
    }

    private void registerSupportBeanProcessors() {
        BeanManager beanManager = rimor.getBeanManager();
        InstructionResolver instructionResolver = new InstructionResolver(rimor.getExecutionContextService());

        CommandProcessor commandProcessor = new CommandProcessor(rimor.getBeanProcessor(), rimor.getCommandRegistry(), instructionResolver);
        beanManager.registerBeanProcessor(commandProcessor);
    }

    private void registerSupportProviders() {
        rimor.registerExecutionContextProvider(new OptionalProvider(rimor.getExecutionContextService()));
    }
}
