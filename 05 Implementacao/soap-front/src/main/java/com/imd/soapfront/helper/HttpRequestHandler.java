package com.imd.soapfront.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestHandler {
    public static ResultHelper sendRequest(String path, String method, String input) throws IOException{
		URL url = new URL(path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod(method);

        if(input != ""){
            OutputStream os = con.getOutputStream();
            os.write(input.getBytes());
            os.flush();
        }

        int status = con.getResponseCode();
		BufferedReader in;

		if (status > 299) {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		} else {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		}

        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine + "\n");
        }
        in.close();
        con.disconnect();

        return ResultHelper.fromJson(content);
    }
}