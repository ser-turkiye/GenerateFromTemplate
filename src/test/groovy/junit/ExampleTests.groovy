package junit

import de.ser.doxis4.agentserver.AgentExecutionResult
import org.junit.*
import ser.KSIForm

class ExampleTests {

    Binding binding

    @BeforeClass
    static void initSessionPool() {
        AgentTester.initSessionPool()
    }

    @Before
    void retrieveBinding() {
        binding = AgentTester.retrieveBinding()
    }

    @Test
    void testForAgentResult() {

        def agent = new KSIForm();

        binding["AGENT_EVENT_OBJECT_CLIENT_ID"] = "ST0aBPM_PLADIS243e194b33-fe89-4b96-92e1-a770c9a0890a182024-04-16T19:51:52.372Z011"

        def result = (AgentExecutionResult)agent.execute(binding.variables)
        System.out.println(result)
//        assert result.resultCode == 0
//        assert result.executionMessage.contains("Linux")
//        assert agent.eventInfObj instanceof IDocument
    }

    @Test
    void testForGroovyAgentMethod() {
//        def agent = new GroovyAgent()
//        agent.initializeGroovyBlueline(binding.variables)
//        assert agent.getServerVersion().contains("Linux")
    }

    @Test
    void testForJavaAgentMethod() {
//        def agent = new JavaAgent()
//        agent.initializeGroovyBlueline(binding.variables)
//        assert agent.getServerVersion().contains("Linux")
    }

    @After
    void releaseBinding() {
        println("RLEASE BINDING RUNNING.....")
        AgentTester.releaseBinding(binding)
    }

    @AfterClass
    static void closeSessionPool() {
        AgentTester.closeSessionPool()
    }
}
