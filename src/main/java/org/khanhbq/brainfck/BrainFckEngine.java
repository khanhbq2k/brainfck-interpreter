package org.khanhbq.brainfck;

import java.io.*;

public class BrainFckEngine {

    static final int DEFAULT_CELL_LENGTH = 100000;

    public BrainFckEngine() {
        this(DEFAULT_CELL_LENGTH, new PrintStream(System.out), System.in);
    }

    public BrainFckEngine(int cellLength) {
        this(cellLength, new PrintStream(System.out), System.in);
    }

    public BrainFckEngine(int cellLength, OutputStream out) {
        this(cellLength, out, System.in);
    }

    public BrainFckEngine(int cellLength, OutputStream out, InputStream in) {
        init(cellLength);
        outputWriter = out;
        consoleReader = new InputStreamReader(in);
    }

    protected void init(int cellLength) {
        data = new byte[cellLength];
        ptr = 0;
        charPointer = 0;
    }

    enum TOKEN {
        NEXT('>'),
        PREVIOUS('<'),
        PLUS('+'),
        MINUS('-'),
        OUTPUT('.'),
        INPUT(','),
        LEFT_BRACKET('['),
        RIGHT_BRACKET(']'),
        ;

        final char identifier;

        TOKEN(char identifier) {
            this.identifier = identifier;
        }

        static TOKEN fromIdentifier(char c) {
            for (TOKEN token : TOKEN.values()) {
                if (token.identifier == c) {
                    return token;
                }
            }
            throw new RuntimeException("TOKEN " + c + " not found!");
        }
    }

    byte[] data;
    int ptr = 0;
    int charPointer = 0;
    BufferedReader fileReader;
    InputStreamReader consoleReader;
    OutputStream outputWriter;
    int lineCount = 0;
    int columnCount = 0;


    public void interpret(File file) throws Exception {
        fileReader = new BufferedReader(new FileReader(file));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = fileReader.readLine()) != null) {
            content.append(line);
            lineCount++;
        }
        interpret(content.toString());
    }

    void interpret(String str) throws Exception {
        char[] cArr = str.toCharArray();
        for (; charPointer < cArr.length; charPointer++) {
            interpret(cArr[charPointer], cArr);
        }
        init(data.length);
    }

    private void interpret(char c, char[] chars) throws Exception {
        TOKEN token = TOKEN.fromIdentifier(c);
        switch (token) {
            case NEXT:
                if ((ptr + 1) > data.length) {
                    throw new Exception("Error on line" + lineCount + ", column " + columnCount + ":"
                            + "data pointer (" + ptr + ") on position " + charPointer + " out of range");
                }
                ptr++;
                break;
            case PREVIOUS:
                if ((ptr - 1) < 0) {
                    throw new Exception("Error on line" + lineCount + ", column " + columnCount + ":"
                            + "data pointer (" + ptr + ") on position " + charPointer + " out of range");
                }
                ptr--;
                break;
            case PLUS:
                data[ptr]++;
                break;
            case MINUS:
                data[ptr]--;
                break;
            case OUTPUT:
                outputWriter.write(data[ptr]);
                break;
            case INPUT:
                data[ptr] = (byte) consoleReader.read();
                break;
            case LEFT_BRACKET:
                if (data[ptr] == 0) {
                    int i = 1;
                    while (i > 0) {
                        TOKEN t = TOKEN.fromIdentifier(chars[++charPointer]);
                        if (TOKEN.RIGHT_BRACKET == t) {
                            i--;
                        } else if (TOKEN.LEFT_BRACKET == t) {
                            i++;
                        }
                    }
                }
                break;
            case RIGHT_BRACKET:
                int i = 1;
                while (i > 0) {
                    TOKEN t = TOKEN.fromIdentifier(chars[--charPointer]);
                    if (TOKEN.RIGHT_BRACKET == t) {
                        i++;
                    } else if (TOKEN.LEFT_BRACKET == t) {
                        i--;
                    }
                }
                charPointer--;
                break;
            default:
                break;
        }
        columnCount++;
    }
}
