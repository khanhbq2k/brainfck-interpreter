import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.khanhbq.brainfck.TrollScriptEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrollScriptEngineTest {

    private TrollScriptEngine trollScriptEngine;

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
        trollScriptEngine = new TrollScriptEngine();
    }

    @Test
    public void testInterpret() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File trollScriptDir = new File(Objects.requireNonNull(classLoader.getResource("trollscript")).getFile());
            File[] files = trollScriptDir.listFiles();

            assert files != null;
            List<File> fileList = Arrays.stream(files)
                    .filter(File::isFile)
                    .collect(Collectors.toList());
            for (File file : fileList) {
                trollScriptEngine.interpret(file);
                assertNotNull(outContent.toString());
                outContent.reset();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
