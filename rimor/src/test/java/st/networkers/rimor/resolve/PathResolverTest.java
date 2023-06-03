package st.networkers.rimor.resolve;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class PathResolverTest {

//    static FooCommand fooCommand = new FooCommand();
//
//    MappedCommand foo;
//    PathResolver resolver;
//
//    @BeforeEach
//    void setUp() {
//        foo = new CommandResolver().resolve(fooCommand);
//        resolver = new PathResolverImpl();
//    }
//
//    @Test
//    void givenPathWithNoMapping_whenResolving_thenResultIsMainInstruction() {
//        ResolvedPath results = resolver.resolvePath(foo, Arrays.asList("test", "param0", "param1"), ExecutionContext.build());
//
//        assertEquals(foo.getMainInstruction().get(), results.getInstruction());
//        assertThat(results.getLeftoverPath()).containsExactly("test", "param0", "param1");
//    }
//
//    @Test
//    void givenBazPath_whenResolving_thenResultIsBazInstruction() {
//        ResolvedPath results = resolver.resolvePath(foo, Arrays.asList("baz", "param0", "param1"), ExecutionContext.build());
//
//        assertEquals(foo.getInstruction("baz").get(), results.getInstruction());
//        assertThat(results.getLeftoverPath()).containsExactly("param0", "param1");
//    }
//
//    @Test
//    void givenBarPath_whenResolving_thenThrowsInstructionNotFoundException() {
//        // throws exception because the bar subcommand has no main instruction
//        InstructionNotFoundException exception = assertThrows(
//                InstructionNotFoundException.class,
//                () -> resolver.resolvePath(foo, Arrays.asList("bar", "test"), ExecutionContext.build())
//        );
//
//        assertEquals(foo.getSubcommand("bar").get(), exception.getSubcommand());
//        assertThat(exception.getRemainingPath()).containsExactly("test");
//    }
//
//    @Test
//    void givenBarFooPath_whenResolving_thenResultIsFooInstructionInBarSubcommand() {
//        Instruction instruction = foo.getSubcommand("bar").get().getInstruction("foo").get();
//        ResolvedPath results = resolver.resolvePath(foo, Arrays.asList("bar", "foo", "true"), ExecutionContext.build());
//
//        assertEquals(instruction, results.getInstruction());
//        assertThat(results.getLeftoverPath()).containsExactly("true");
//    }
}
