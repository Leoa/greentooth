package ws;

import java.io.IOException;
import java.io.InputStream;

public class Frame {

    private final InputStream in;
    private static final int FIRST_BYTE = 0x00;
    private static final int LAST_BYTE = 0xFF;
    private StringBuilder builder;

    public Frame(InputStream in) {
        this.in = in;
        builder = new StringBuilder();
    }

    public void read() throws IOException {
        int first = in.read();
        if (first != FIRST_BYTE) {
            throw new IOException("Frame should start with 0x00");
        }

        readUntilFinished();
    }

    private void readUntilFinished() throws IOException {
        long l = in.available();
        byte[] bytes = new byte[(int)l];
        int i = in.read();
        int j = 0;
        while (i != -1) {
            if (i == LAST_BYTE) {
                break;
            }
            i = in.read();
            bytes[++j] = (byte) i;
        }
        if (i != LAST_BYTE) {
            throw new IOException("Frame should finish with 0xFF, was " + i);
        }
        builder.append(new String(bytes, "UTF-8"));
    }

    public String getMessage() {
        return builder.toString();
    }

    public static class IncorrectMessage extends RuntimeException {
    }
}
