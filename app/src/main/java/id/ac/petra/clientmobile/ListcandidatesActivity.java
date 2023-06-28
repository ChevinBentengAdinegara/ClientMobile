package id.ac.petra.clientmobile;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListcandidatesActivity extends AppCompatActivity {

    private ListView listcandidates;
    private Button pesanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_candidates);
        listcandidates = findViewById(R.id.listcandidates);

        new FetchcandidatesDataTask().execute();
    }

    private class FetchcandidatesDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result;
            try {
                URL url = new URL("http://192.168.0.106:7000/candidates");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    result = response.toString();
                } else {
                    result = "Error: " + responseCode;
                }
                connection.disconnect();
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            List<String> candidatesList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject candidatesObject = jsonArray.getJSONObject(i);
                    String namacandidates = candidatesObject.getString("namacandidates");
                    String id = candidatesObject.getString("id");

                    String candidatesDetails = "Nama candidates: " + namacandidates + "\n" + "id: " + id +"\n" +
                            "";

                    candidatesList.add(candidatesDetails);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ListcandidatesActivity.this,
                        android.R.layout.simple_list_item_1, candidatesList);
                listcandidates.setAdapter(adapter);

            } catch (JSONException e) {
                Toast.makeText(ListcandidatesActivity.this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}