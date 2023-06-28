package id.ac.petra.clientmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button ListCandidatesButton;
    private Button PesanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ListCandidatesButton = findViewById(R.id.ListCandidatesButton);
        PesanButton = findViewById(R.id.PesanButton);

        ListCandidatesButton.setOnClickListener(this);
        PesanButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ListCandidatesButton:
                openListCandidatesActivity();
                break;
            case R.id.PesanButton:
                openPesanActivity();
                break;
        }
    }

    private void openListCandidatesActivity() {
        Intent intent = new Intent(this, ListcandidatesActivity.class);
        startActivity(intent);
    }

    private void openPesanActivity() {
        Intent intent = new Intent(this, PesanActivity.class);
        startActivity(intent);
    }
}