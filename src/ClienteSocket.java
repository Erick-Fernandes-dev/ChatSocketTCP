import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;



public class ClienteSocket {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    /**
     * @param socket
     * @throws Exception
     */
    public ClienteSocket(Socket socket) throws Exception {
        this.socket = socket;
        System.out.println("Cliente " + socket.getRemoteSocketAddress() + " foi conectado");

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);

    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    public void close() {

        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao fechar socket: " + e.getMessage());
        }
    }

    public String pegarMenssagem() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    public boolean enviarMensagem(String msg) {
        out.println(msg);

        return out.checkError();
    }
    
}
