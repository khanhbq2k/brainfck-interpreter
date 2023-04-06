package org.khanhbq.brainfck;

import java.util.ArrayList;
import java.util.List;

public class TrollScriptEngine extends BrainFckEngine {

    static final int DEFAULT_TOKEN_LENGTH = 3;

    enum TOKEN {
        START("tro"),
        NEXT("ooo"),
        PREVIOUS("ool"),
        PLUS("olo"),
        MINUS("oll"),
        OUTPUT("loo"),
        INPUT("lol"),
        LEFT_BRACKET("llo"),
        RIGHT_BRACKET("lll"),
        END("ll."),
        ;

        final String identifier;

        TOKEN(String identifier) {
            this.identifier = identifier;
        }

        static TrollScriptEngine.TOKEN fromIdentifier(String str) {
            for (TrollScriptEngine.TOKEN token : TrollScriptEngine.TOKEN.values()) {
                if (token.identifier.equals(str)) {
                    return token;
                }
            }
            throw new RuntimeException("TOKEN " + str + " not found!");
        }
    }

    public TrollScriptEngine() {
        super();
    }

    @Override
    void interpret(String str) throws Exception {
        boolean started = false;
        List<TOKEN> tokens = new ArrayList<>();
        while (ptr < str.length()) {
            String tokenStr = "";
            if (ptr + DEFAULT_TOKEN_LENGTH <= str.length()) {
                tokenStr = str.substring(ptr, ptr + DEFAULT_TOKEN_LENGTH);
            } else {
                tokenStr = str.substring(ptr, ptr + (str.length() - ptr));
            }
            TOKEN token = TOKEN.fromIdentifier(tokenStr);
            if (token == TOKEN.START) {
                started = true;
            } else if (token == TOKEN.END) {
                break;
            } else if (started) {
                tokens.add(token);
            }
            ptr += DEFAULT_TOKEN_LENGTH;
        }

        for (int tokenPointer = 0; tokenPointer < tokens.size(); ) {
            TOKEN token = tokens.get(tokenPointer);

            if (token == TOKEN.NEXT) {
                ptr = (ptr == data.length - 1 ? 0 : ptr + 1);
            }
            if (token == TOKEN.PREVIOUS) {
                ptr = (ptr == 0 ? data.length - 1 : ptr - 1);
            }
            if (token == TOKEN.PLUS) {
                data[ptr]++;
            }
            if (token == TOKEN.MINUS) {
                data[ptr]--;
            }
            if (token == TOKEN.OUTPUT) {
                outputWriter.write((char) data[ptr]);
                outputWriter.flush();
            }
            if (token == TOKEN.INPUT) {
                data[ptr] = (byte) consoleReader.read();
            }
            if (token == TOKEN.LEFT_BRACKET) {
                if (data[ptr] == 0) {
                    int level = 1;
                    while (level > 0) {
                        tokenPointer++;
                        if (tokens.get(tokenPointer) == TOKEN.LEFT_BRACKET)
                            level++;
                        else if (tokens.get(tokenPointer) == TOKEN.RIGHT_BRACKET)
                            level--;
                    }
                }
            }
            if (token == TOKEN.RIGHT_BRACKET) {
                if (data[ptr] != 0) {
                    int level = 1;
                    while (level > 0) {
                        tokenPointer--;
                        if (tokens.get(tokenPointer) == TOKEN.LEFT_BRACKET)
                            level--;
                        else if (tokens.get(tokenPointer) == TOKEN.RIGHT_BRACKET)
                            level++;
                    }
                }
            }

            tokenPointer++;
        }
        // reset to init state
        init(data.length);
    }
}
