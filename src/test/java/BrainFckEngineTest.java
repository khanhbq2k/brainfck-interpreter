import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khanhbq.brainfck.BrainFckEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrainFckEngineTest {

    private BrainFckEngine brainFckEngine;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @BeforeEach
    public void setupEngine() {
        brainFckEngine = new BrainFckEngine();
    }

    @Test
    public void testInterpret() {
        try {
            URL url = BrainFckEngineTest.class.getResource("/brainfck/hello-world.bf");
            assert url != null;
            File file = new File(url.getFile());

            brainFckEngine.interpret(file);
            assertEquals("Hello World!\n", outContent.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
