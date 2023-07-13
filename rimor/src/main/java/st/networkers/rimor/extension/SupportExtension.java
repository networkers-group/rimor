package st.networkers.rimor.extension;

import st.networkers.rimor.Rimor;
import st.networkers.rimor.bean.BeanManager;
import st.networkers.rimor.command.CommandProcessor;
import st.networkers.rimor.inject.provide.support.OptionalProvider;
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

        CommandProcessor commandProcessor = new CommandProcessor(rimor.getCommandRegistry(), new InstructionResolver(rimor.getInjector()));
        beanManager.registerBeanProcessor(commandProcessor);
    }

    private void registerSupportProviders() {
        rimor.registerProvider(new OptionalProvider(rimor.getInjector()));
    }
}
