import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

         // Read HTTP request from the client
         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         String line;
         StringBuilder requestBuilder = new StringBuilder();
         while (!(line = in.readLine()).isEmpty()) {
           requestBuilder.append(line + "\n");
         }
         String request = requestBuilder.toString();
         System.out.println("Request from client:\n"+ request);

         String[] requestLines = request.split("\n");
         System.out.println("-------> "+requestLines[0]);

         String startLine = requestLines[0];
         String[] splitStartLine = startLine.split(" ");
         String path = splitStartLine[1];
         System.out.println("-------> " + path);

         String httpResponse="";
         if(path.equals("/")){
           httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
         } else if (path.startsWith("/echo/")) {
           String responseString = path.substring(6);
           System.out.println(responseString);
           httpResponse = "HTTP/1.1 200 OK\r\n"+
                   "Content-Type: text/plain\r\n"+
                   "Content-Length: " +responseString.getBytes(StandardCharsets.UTF_8).length+ "\r\n"+
                   "\r\n"+
                   responseString;
           System.out.println(httpResponse);
         } else if (path.startsWith("/user-agent")) {
           String header = requestLines[3];
           System.out.println("Header  " + header);
           String[] values = header.split(":");
           String value = values[1];
           httpResponse = "HTTP/1.1 200 OK\r\n"+
                   "Content-Type: text/plain\r\n"+
                   "Content-Length: " +value.getBytes(StandardCharsets.UTF_8).length+ "\r\n"+
                   "\r\n"+
                   value;
           System.out.println(value);

         } else {
           httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
         }

         OutputStream outputStream = clientSocket.getOutputStream();
         outputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));
         outputStream.flush();
         in.close();
       }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}
