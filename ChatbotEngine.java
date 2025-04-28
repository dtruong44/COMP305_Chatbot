import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
//needs open api and json dependencies

public class ChatbotEngine {

    static String apiKey = "";


    public static void main(String[] args) throws Exception {

        apiKey = loadEnv("APIKEY.env");

        System.out.println(createResponse("how many turtles in ocean?"));

    }
    /**
     * creates a response to the given input string using open ai api calls
     *
     * @param userInput : the message to generate a response to
     *
     * @return a string response from chatbot
     */
    public static String createResponse(String userInput) throws Exception{

        apiKey = loadEnv("APIKEY.env");

        String body = String.format("""
        {
          "model": "gpt-4",
          "messages": [{"role": "user", "content": "%s"}]
        }
        """,userInput);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();

        // Find the start and end index of the assistant's content in the JSON response
        String startMarker = "\"content\": \"";
        String endMarker = "\"}";

        int startIndex = responseBody.indexOf(startMarker) + startMarker.length();
        int endIndex = startIndex+1;
        boolean insideEscapedQuote = false;

        // Loop through the characters to find the correct end quote
        while (endIndex < responseBody.length()) {
            char currentChar = responseBody.charAt(endIndex);

            // Check if we encounter an escaped quote
            if (currentChar == '\\' && endIndex + 1 < responseBody.length() && responseBody.charAt(endIndex + 1) == '"') {
                // Skip the escaped quote
                insideEscapedQuote = true;
                endIndex += 2;  // Move past the escaped quote
            } else if (currentChar == '"' && !insideEscapedQuote) {
                // If it's not an escaped quote, we've reached the end of the content
                break;
            } else {
                // Reset the flag if we are no longer inside an escaped quote
                insideEscapedQuote = false;
                endIndex++;
            }
        }

        // Extract the content (assistant's message)
        //System.out.println(String.format("%d, %d",startIndex,endIndex));
        return responseBody.substring(startIndex, endIndex);

    }
    /**
     *
     * loads the api key from a .env file
     *
     * @param filePath path to the file
     *
     * @return api key
     */

    public static String loadEnv(String filePath) throws Exception {
        String env = "";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            env = line.substring(8).strip();
        }

        return env;
    }
}
    
