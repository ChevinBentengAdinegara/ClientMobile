package id.ac.petra.clientmobile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PesanActivity extends AppCompatActivity {

    private EditText voterField;
    private EditText idField;
    private EditText candidatesField;
    private Spinner jenicandidatesField;
    private Button pesanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan);

        voterField = findViewById(R.id.voterField);
        idField = findViewById(R.id.idField);
        candidatesField = findViewById(R.id.candidatesField);
        jenicandidatesField = findViewById(R.id.jenicandidatesField);
        pesanButton = findViewById(R.id.pesanButton);

        List<String> jenis = new ArrayList<>();
        jenis.add("candidates 1");
        jenis.add("candidates 2");

        ArrayAdapter<String> jenisAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenis);
        jenisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenicandidatesField.setAdapter(jenisAdapter);

        pesanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voter = voterField.getText().toString().trim();
                String id = idField.getText().toString().trim();
                String candidates = candidatesField.getText().toString().trim();
                String jenistiket = jenicandidatesField.getSelectedItem().toString().trim();

                if (voter.isEmpty() || id.isEmpty() || candidates.isEmpty() || jenistiket.isEmpty()) {
                    Toast.makeText(PesanActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject bookingData = new JSONObject();
                    try {
                        bookingData.put("voter", voter);
                        bookingData.put("id", id);
                        bookingData.put("candidates", candidates);
                        bookingData.put("jenistiket",jenistiket);

                        new BookAsyncTask().execute(bookingData.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class BookAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                String bookingData = params[0];
                URL url = new URL("http://192.168.0.106:7000/votes");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                os.write(bookingData.getBytes());
                os.flush();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    result = responseBuilder.toString();
                } else {
                    result = "Error: " + responseCode;
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error occurred while connecting to the server.";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(PesanActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}