package block.chain.IPC;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 5050;

        ServerSocket ssk = new ServerSocket(port);

        while(true){
            Socket socket = ssk.accept();

        }
    }
}
