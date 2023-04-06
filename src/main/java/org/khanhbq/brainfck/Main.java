package org.khanhbq.brainfck;

import java.io.File;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws Exception {
        URL url = Main.class.getResource("/trollscript/art.troll");
        assert url != null;
        File file = new File(url.getFile());

        TrollScriptEngine trollScriptEngine = new TrollScriptEngine();
        trollScriptEngine.interpret(file);
    }
}
