package st.networkers.rimor.aop;

class CommandExecutorTest {

//    static MappedCommand foo;
//
//    @Mock Method method;
//    CommandExecutor commandExecutor;
//
//    @BeforeAll
//    static void beforeAll() {
//        foo = new CommandResolver().resolve(new FooCommand());
//    }
//
//    @BeforeEach
//    void setUp() {
//        commandExecutor = new CommandExecutorImpl(new RimorInjectorImpl(), new ExceptionHandlerRegistry(), new ExecutionTaskRegistry());
//    }
//
//    @Test
//    void givenQuxInstructionWithIntegerAs1_whenExecuting_returns1() {
//        ExecutionContext context = ExecutionContext.build(
//                new ContextComponent<>(Integer.class, 1)
//        );
//
//        assertThat(commandExecutor.execute(foo.getInstruction("qux").get(), context)).isEqualTo(1);
//    }
//
//    @Test
//    void givenQuxInstructionWithBarAnnotationAnnotatedString_whenExecuting_thenThrowsIllegalStateException() {
//        ExecutionContext context = ExecutionContext.build(
//                new ContextComponent<>(Integer.class, 1),
//                new ContextComponent<>(String.class, "").annotatedWith(BarAnnotation.class)
//        );
//
//        assertThrows(IllegalStateException.class, () -> commandExecutor.execute(foo.getInstruction("bazAlias").get(), context));
//    }
//
//    @Test
//    void givenSetInstructionFromBarSubcommandWithTrueBoolean_whenExecuting_thenReturnsTrue() {
//        ExecutionContext context = ExecutionContext.build(
//                new ContextComponent<>(Boolean.class, true)
//        );
//
//        Instruction bar = foo.getSubcommand("bar").get().getInstruction("set").get();
//        assertEquals(true, commandExecutor.execute(bar, context));
//    }
//
//    @Test
//    void givenSetInstructionFromBarSubcommandWithBarAnnotationAnnotatedString_whenExecuting_thenThrowsIllegalStateException() {
//        ExecutionContext context = ExecutionContext.build(
//                new ContextComponent<>(Boolean.class, true),
//                new ContextComponent<>(String.class, "").annotatedWith(BarAnnotation.class)
//        );
//
//        Instruction bar = foo.getSubcommand("bar").get().getInstruction("set").get();
//        assertThrows(IllegalStateException.class, () -> commandExecutor.execute(bar, context));
//    }
}
