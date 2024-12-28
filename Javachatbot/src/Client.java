import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost"; // Ensure this matches the server's address
    private static final int SERVER_PORT = 1234; // Ensure this matches the server's port

    public static void main(String[] args) {
        try {
            System.out.println("Attempting to connect to the server at " + SERVER_ADDRESS + ":" + SERVER_PORT);

            // Create a socket to connect to the server
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the chat server!");

            // Set up input and output streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a thread to handle incoming messages
            new Thread(() -> {
                try {
                    String serverResponse;
                    while ((serverResponse = in.readLine()) != null) {
                        System.out.println("Server: " + serverResponse);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
            }).start();

            // Read messages from the console and send them to the server
            Scanner scanner = new Scanner(System.in);
            String userInput;
            while (true) {
                System.out.print("You: ");
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the chat...");
                    break;
                }

                out.println(userInput);
            }

            // Close resources after exiting
            out.close();
            in.close();
            socket.close();
            scanner.close();
            System.out.println("Disconnected from the server.");
        } catch (ConnectException e) {
            System.err.println("Connection failed: " + e.getMessage() + ". Ensure the server is running.");
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}
