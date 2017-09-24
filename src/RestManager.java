import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RestManager {

	public static void setCredencials(String user, String pass) {
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication (user, pass.toCharArray());
		    }
		});
	}
	
	public static String sendGet(String url) {

		// String url = "http://www.google.com/search?q=mkyong";

		// optional default is GET
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			// add request header
			// con.setRequestProperty("User-Agent", USER_AGENT);
			int responseCode;
			try {
				responseCode = con.getResponseCode();
			} catch (java.net.ConnectException e1) {
				System.err.println("The connection was refused");
				return "error D:";
			}
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			if (responseCode != 200) {
				return "Error: " + responseCode;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			//Thread.dumpStack();
			// print result
			// System.out.println(response.toString());
			return response.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error rip";

	}

	// HTTP POST request
	public static String sendPost(String url, String[] querys, String[] params) throws Exception {

		// String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		// con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "";
		for (int i = 0; i < querys.length; i++) {
			urlParameters += querys[i] + "=" + params[i] + "&";
		}

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		// System.out.println(response.toString());
		return response.toString();
	}

}
