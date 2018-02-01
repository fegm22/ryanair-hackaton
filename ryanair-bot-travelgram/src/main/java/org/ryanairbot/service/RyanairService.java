package org.ryanairbot.service;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class RyanairService {

    @Value("${ryanair.commandservice.url}")
    private String commandServiceUrl;

    private final InternalRyanairService internalRyanairService;

    public RyanairService(InternalRyanairService internalRyanairService) {
        this.internalRyanairService = internalRyanairService;
    }

    /**
     * This method will return the response base on the questions ask in telegram
     *
     * @param query Text from Telegram
     * @return Return the answer
     */
    public String processMessage(String query) throws RuntimeException {

        if (query.toUpperCase().contains("BOT")) {
            return internalRyanairService.processMessage(query);
        } else {
            return getRequestCommand(query);
        }
    }

    private String getRequestCommand(String query) throws RuntimeException {

        try {
            String completeURL = commandServiceUrl + URLEncoder.encode("", "UTF-8");

            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            HttpPost request = new HttpPost(completeURL);

            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("message", query));
            request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf, "UTF-8");

            JSONObject jsonObject = new JSONObject(responseString);

            return jsonObject.getString("header") + "\n\n" + jsonObject.getString("message");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
