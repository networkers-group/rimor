package st.networkers.rimor.context.provide;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import st.networkers.rimor.FooAnnotation;
import st.networkers.rimor.FooAnnotationImpl;
import st.networkers.rimor.bean.BeanProcessingException;
import st.networkers.rimor.bean.RimorConfiguration;
import st.networkers.rimor.context.ExecutionContextService;
import st.networkers.rimor.qualify.reflect.QualifiedMethod;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class ProvidesContextProcessorTest {

    @Mock ExecutionContextService executionContextService;
    @InjectMocks ProvidesContextProcessor providesContextProcessor;

    @ProvidesContext({CharSequence.class, String.class})
    public String provideFoo() {
        return "foo";
    }

    @Test
    void givenValidContextProviderMethod_whenProcessing_beanAndMethodAreRight() throws NoSuchMethodException {
        Method method = this.getClass().getMethod("provideFoo");
        HandlerMethodExecutionContextProvider provider = providesContextProcessor.processMethod(method, this);

        assertThat(provider.getBean()).isEqualTo(this);
        assertThat(provider.getMethod()).isEqualTo(QualifiedMethod.build(method));
    }

    @Test
    void givenContextProviderMethodWithProvidedTypes_whenProcessing_providedTypesAreThem() throws NoSuchMethodException {
        Method method = this.getClass().getMethod("provideFoo");
        HandlerMethodExecutionContextProvider provider = providesContextProcessor.processMethod(method, this);

        assertThat(provider.getProvidedTypes()).containsExactly(CharSequence.class, String.class);
    }

    @ProvidesContext
    public String contextProviderWithEmptyProvidedTypes(String injectedStringFromContext) {
        return "foo";
    }

    @Test
    void givenContextProviderMethodWithEmptyProvidedTypes_whenProcessing_providedTypeIsMethodReturnType() throws NoSuchMethodException {
        Method method = this.getClass().getMethod("contextProviderWithEmptyProvidedTypes", String.class);
        HandlerMethodExecutionContextProvider provider = providesContextProcessor.processMethod(method, this);

        assertThat(provider.getProvidedTypes()).containsExactly(String.class);
    }

    @ProvidesContext
    @FooAnnotation("bar")
    public String contextProviderAnnotatedWithFooAnnotation() {
        return "foo";
    }

    @Test
    void givenContextProviderMethodAnnotatedWithFooAnnotation_whenProcessing_containsFooAnnotation() throws NoSuchMethodException {
        Method method = this.getClass().getMethod("contextProviderAnnotatedWithFooAnnotation");
        HandlerMethodExecutionContextProvider provider = providesContextProcessor.processMethod(method, this);

        assertThat(provider.getQualifiers()).containsExactly(new FooAnnotationImpl("bar"));
    }

    @ProvidesContext
    public void voidContextProvider() {
    }

    @Test
    void givenVoidContextProviderMethod_whenProcessing_throwsBeanProcessingException() throws NoSuchMethodException {
        Method method = this.getClass().getMethod("voidContextProvider");

        assertThatCode(() -> providesContextProcessor.processMethod(method, this))
                .isInstanceOf(BeanProcessingException.class)
                .hasMessageContaining("cannot provide void");
    }

    @ProvidesContext(int.class) // not providing the same type or a supertype of the method's return type, not possible
    public Number contextProviderWithIncompatibleProvidedTypes() {
        return -1;
    }

    @Test
    void givenContextProviderMethodWithIncompatibleProvidedTypes_whenProcessing_throwsBeanProcessingException() throws NoSuchMethodException {
        Method method = this.getClass().getMethod("contextProviderWithIncompatibleProvidedTypes");

        assertThatCode(() -> providesContextProcessor.processMethod(method, this))
                .isInstanceOf(BeanProcessingException.class)
                .hasMessageContaining("'s return type is not compatible with provided type " + int.class);
    }

    @RimorConfiguration
    static class GlobalProviders {
        @ProvidesContext({CharSequence.class, String.class})
        public String provideFoo() {
            return "foo";
        }
    }

    @Mock HandlerMethodExecutionContextProvider mockedProvider;

    @Test
    void givenContextProviderMethodInClassAnnotatedWithRimorConfiguration_whenProcessingBean_thenProviderIsRegisteredGlobally() {
        providesContextProcessor.registerContextProvider(mockedProvider, new GlobalProviders());

        verify(executionContextService).registerGlobalExecutionContextProvider(mockedProvider);
        verify(executionContextService, never()).registerExecutionContextProvider(any(), any());
    }

    static class BeanWithLocalProvider {
        @ProvidesContext({CharSequence.class, String.class})
        public String provideFoo() {
            return "foo";
        }
    }

    @Test
    void givenContextProviderMethodInClassNotAnnotatedWithRimorConfiguration_whenProcessingBean_thenProviderIsRegisteredLocally() {
        BeanWithLocalProvider beanWithLocalProvider = new BeanWithLocalProvider();
        providesContextProcessor.registerContextProvider(mockedProvider, beanWithLocalProvider);

        verify(executionContextService).registerExecutionContextProvider(mockedProvider, beanWithLocalProvider);
        verify(executionContextService, never()).registerGlobalExecutionContextProvider(any());
    }
}
