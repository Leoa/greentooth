package ws;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class WebSocket {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Listener listener;
    private ArrayList<Object> eventQueue = new ArrayList<Object>();

    public interface Event {
    }

    public class CloseEvent implements Event {
    }

    public class OpenEvent implements Event {
    }


    public interface Listener {
        void onClose(WebSocket socket, CloseEvent event);

        void onMessage(WebSocket socket, MessageEvent event);

        void onOpen(WebSocket socket, OpenEvent event);
    }

    private WebSocket(String url, String protocol) throws IOException {
        URI uri = URI.create(url);
        uri.getScheme();


        int cut = url.indexOf('/', 5);
        if (cut == -1) {
            cut = url.length();
        }

        final String path = uri.getPath();
        final String host  =uri.getHost(); //url.substring(cut);
        String hostAndPort = url.substring(5, cut);
        int port = uri.getPort();


        System.out.println("Host: " + host);
        System.out.println("Port: " + port);
        System.out.println("Path: " + path);

        if (port == 443) {
            try {
                socket = SSLContext.getDefault().getSocketFactory().createSocket(host, port);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            socket = new Socket(host, port);
        }

        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();

        new Thread(new Runnable() {
            public void run() {
                try {
                    println("GET /" + path + " HTTP/1.1");
                    println("Upgrade: WebSocket");
                    println("Connection: Upgrade");
                    println("Host: " + host);
                    println("Origin: http://" + host);
                    println("WebSocket-Protocol: " + "sample");
                    println("");

                    while (true) {
                        String l = readln();
                        System.out.println("Reading: " + l);
                        if (l.length() == 0) {
                            break;
                        }
                    }

                    fire(new OpenEvent());
                    while (true) {
                        String s = readFrame();
                        fire(new MessageEvent(s));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    fire(new CloseEvent());
                    //close();
                }
            }
        }).start();
    }

    private void fire(Object event) {
        System.out.println("fireing event: " + event);
        if (event != null) {
            eventQueue.add(event);
        }
        if (listener != null) {
            for (Object o : eventQueue) {
                if (o instanceof MessageEvent) {
                    listener.onMessage(this, (MessageEvent) o);
                } else if (o instanceof CloseEvent) {
                    listener.onClose(this, (CloseEvent) o);
                } else if (o instanceof OpenEvent) {
                    listener.onOpen(this, (OpenEvent) o);
                }
            }
            eventQueue.clear();
        }
    }

    private String readln() throws IOException {
        StringBuilder sb = new StringBuilder();

        while (true) {
            int i = inputStream.read();
            if (i == -1) {
                throw new IOException("closed");
            } else if (i == '\r') {
            } else if (i == '\n') {
                break;
            } else {
                sb.append((char) i);
            }
        }
        return sb.toString();
    }


    private String readFrame() throws IOException {
        int i = inputStream.read();
        if (i == -1) {
            throw new IOException("closed by remote");
        }
        if (i != 0) {
            System.out.println("Illegal frame start: " + i);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            i = inputStream.read();
            if (i == -1) {
                throw new IOException("closed by remote (in frame)");
            }
            if (i == 255) {
                break;
            }
            baos.write(i);
        }
        return new String(baos.toByteArray(), "UTF-8");

    }


    private void println(String string) throws IOException {
        outputStream.write(string.getBytes("UTF-8"));
        outputStream.write('\r');
        outputStream.write('\n');
        if (string.length() == 0) {
            outputStream.flush();
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
        fire(null);
    }

    public void close() throws IOException {
        socket.close();
    }

    public static WebSocket create(String url, String string) throws IOException {
        return new WebSocket(url, string);
    }

    public void send(String s) throws IOException {
        outputStream.write(0);
        outputStream.write(s.getBytes("UTF-8"));
        outputStream.write(255);
    }

}