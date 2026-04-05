import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkScanner {

    // 1. Nmap Scanner Method (TCP/UDP)
    public static void runNmapScan(String target) {
        System.out.println("\n--- Starting Nmap Scan on: " + target + " ---");
        try {
            // -sS (TCP), -sU (UDP), -T4 (Speed)
            // Note: Windows par UDP scan ke liye Admin rights chahiye
            String command = "nmap -sS -sU -T4 " + target;
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            System.out.println("Nmap Error: " + e.getMessage());
        }
    }

    // 2. Simple SQL Injection Checker
    public static void checkSQLi(String testUrl) {
        System.out.println("\n--- Testing SQL Injection on: " + testUrl + " ---");
        String payload = "'"; // Single quote to break SQL query
        try {
            URL url = new URL(testUrl + payload);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Common SQL Error Messages check karna
            String response = content.toString().toLowerCase();
            if (response.contains("sql syntax") || response.contains("mysql_fetch") || response.contains("unclosed quotation mark")) {
                System.out.println("[!] ALERT: Potential SQL Injection Vulnerability Detected!");
            } else {
                System.out.println("[+] No obvious SQL errors found.");
            }

        } catch (Exception e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String targetIP = "127.0.0.1"; // Change this to your target
        String targetWeb = "http://testphp.vulnweb.com/listproducts.php?cat=";

        // Run Scans
        runNmapScan(targetIP);
        checkSQLi(targetWeb);
    }
}