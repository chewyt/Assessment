package chewyt;

import static chewyt.Constants.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        String[] docRoot = DEFAULT_DOCROOT;

        if (args.length > 0) {
            switch (args.length) {
                case 1:

                    break;
                case 2:
                    if (args[0].equals("--port")) {
                        port = Integer.parseInt(args[1]);
                    }
                    break;
                case 3:
                    if (args[0].equals("--port")) {
                        port = Integer.parseInt(args[1]);
                    }
                    break;
                case 4:
                    if (args[0].equals("--port")) {
                        port = Integer.parseInt(args[1]);
                    }
                    if (args[2].equals("--docRoot")) {
                        docRoot = args[3].split(":");
                    }
                    break;
            }
        }
        // Testing for task 3 completed
        // System.out.println("PORT: "+port);
        // System.out.println("docRoot: ");
        // for (String string : docRoot) {
        // System.out.print(string+" ");
        // }

        // Task 4 -Server connection
        HttpServer server = new HttpServer(docRoot, port);
        server.start();

    }
}
