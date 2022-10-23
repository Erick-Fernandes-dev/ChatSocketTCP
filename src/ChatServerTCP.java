import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChatServerTCP {


    private static final int PORTA = 6000;
    private ServerSocket serverSocket;
    private final List<ClienteSocket> clientes = new LinkedList<>();
    //Usando o linkedList, pois não sei quantos cliente serão conectados.


    public void start() throws Exception {
        serverSocket = new ServerSocket(PORTA);
        System.out.println("Servidor iniciado na porta: " + PORTA);
        connectionLoop();
        
    }
    
    //Aguardando as conexões
    private void connectionLoop() throws Exception {
        
        
        while(true) {
            //Aguardar o cliente conectar (Socket Local)
            ClienteSocket clientSocket =  new ClienteSocket(serverSocket.accept());//operação bloqueante
            clientes.add(clientSocket);
            //Aguardar mensagem do cliente
            // tmb é bloqueante
            // String msg = in.readLine();//ler e mostrar a mensagem do cliente
            new Thread(() -> clienteMensagemLoop(clientSocket)).start();
        }
    }

    //aguarda as mensagens do cliente
    private void clienteMensagemLoop(ClienteSocket clienteSocket) {
        String msg;

        try {

            while ((msg = clienteSocket.pegarMenssagem()) != null) {
    
                if ("sair".equalsIgnoreCase(msg)) return;
    
                System.out.printf("Mensagem recebida do cliente %s: %s\n", clienteSocket.getRemoteSocketAddress() , msg);

                enviarMensagemParaTodos(clienteSocket, msg);
            }
        } finally {
            clienteSocket.close();
        }

        


    }

    //Enviar mensagens para todos
    private void enviarMensagemParaTodos(ClienteSocket sender, String msg) {
        Iterator<ClienteSocket> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            ClienteSocket clienteSocket = iterator.next();
            if (!sender.equals(clienteSocket)) {
                // clienteSocket.enviarMensagem(msg);
                if(!clienteSocket.enviarMensagem("Cliente " + sender.getRemoteSocketAddress() + ": " +  msg)) {
                    iterator.remove();
                }
            } 
        }

    }


    public static void main(String[] args)  {
        ChatServerTCP server = new ChatServerTCP();
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Error durante o inicio do servidor: " + e.getMessage());
        }
        System.out.println("Servidor finalizado");
    }

    public static int getPorta() {
        return PORTA;
    }

}
