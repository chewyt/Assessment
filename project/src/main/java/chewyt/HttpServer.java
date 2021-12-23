package chewyt;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer implements Runnable {

    private final Integer port;
    private final String[] docRoot;
    private ServerSocket server;
    private Socket socket;
    private List<String> paths;
    private ExecutorService pool;

    // public HttpServer(){
    // this(DEFAULT_DOCROOT,DEFAULT_PORT);
    // } QUESTION to sir

    public HttpServer(String[] docRoot, int port) {
        this.port = port;
        this.docRoot = docRoot;
    }

    public void start() {
        paths = new LinkedList<>();
        // Check for docRoot path conditions
        for (String path : docRoot) {
            File f = new File(path);

            if (!(f.exists() && f.isDirectory() && f.canRead())) {
                System.err.println("[ERROR] " + f + " is either not a directory, does not exists or not readable");
                System.exit(1);
            }
            paths.add(path);
        }

        this.pool = Executors.newFixedThreadPool(3);
        this.pool.submit(this);

    }

    @Override
    public void run() {

        System.out.printf("[SERVER started. Listening for port %d from %s\n]", port, this.paths);

        try {
            server = new ServerSocket(port);
            while (!server.isClosed()) {
                socket = server.accept();
                System.out.println("Client browser connected");
                HttpClientConnection clientconnection = new HttpClientConnection(socket, this.docRoot);
                pool.submit(clientconnection);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
            }

        }

    }

}
