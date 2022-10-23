import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClientTCP implements Runnable {

    private static final String ENDERECO = "127.0.0.2";
    private ClienteSocket clientSocket;
    private Scanner scanner;


    public ChatClientTCP() {
        scanner = new Scanner(System.in);
    }




    public void start() throws Exception {

        try {
            clientSocket = new ClienteSocket(new Socket(ENDERECO, ChatServerTCP.getPorta()));
            //ENVIAR MENSAGENS
            // this.out = new PrintWriter(clientSocket.getOutputStream(), true);//segundo parametro é o AutoFlush
            // clientSocket.getOutputStream().write();;//saida de fluxo de dados.
            System.out.println("Cliente conectado ao servidor em " + ENDERECO + ":" + ChatServerTCP.getPorta());
            new Thread(this).start();
            loopMessage();

        } finally {
            clientSocket.close();
        }


    }

    @Override
    public void run() {
        String msg;

        while((msg = clientSocket.pegarMenssagem()) != null) {
            System.out.printf("Mensagem recebida do servidor: %s\n", msg);
        }

    }
    
    private void loopMessage() throws Exception {
        String msg;

        do {
            System.out.print("Digite uma mensagem (ou sair para finalizar): ");
            msg = scanner.nextLine();
            clientSocket.enviarMensagem(msg);
            // out.write(msg);//Enviar mensagem
            // out.newLine();//quebrar 1 linha
            // out.flush();//confirma de fato que a mensagem foi enviada

        } while (!msg.equalsIgnoreCase("sair"));
    }



    public static void main(String[] args) {

        ChatClientTCP client = new ChatClientTCP();
        try {
            client.start();
        } catch (Exception e) {
            
            System.out.println("Erro ao iniciar o cliente: " + e.getMessage());
        }

        System.out.println("Cliente finalizado com sucesso!");
    }




    
}
