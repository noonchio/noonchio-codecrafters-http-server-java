import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args) {
    System.out.println("Logs from your program will appear here!");

     ServerSocket serverSocket = null;
     Socket clientSocket = null;

     try {
       serverSocket = new ServerSocket(4221);
       System.out.println("Listening fro connections on port 4221...");

       // forever loop to keep the server running
       while (true) {
         serverSocket.setReuseAddress(true);
         clientSocket = serverSocket.accept(); // Wait for connection from client.
         System.out.println("accepted new connection from " + clientSocket);

         //Prepare HTTP response
         String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";

         OutputStream outputStream = clientSocket.getOutputStream();

         outputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));
         outputStream.flush();
       }




     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
