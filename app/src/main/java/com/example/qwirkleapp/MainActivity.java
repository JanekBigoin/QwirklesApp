package com.example.qwirkleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner playerCountSpinner;
    private LinearLayout playersLayout;
    private List<EditText> playerNameFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerCountSpinner = findViewById(R.id.playerCountSpinner);
        playersLayout = findViewById(R.id.playersLayout);
        playerNameFields = new ArrayList<>();
        TextView choosePlayerCountText = findViewById(R.id.choosePlayerCountText);

        // Créer un adaptateur pour le Spinner (nombre de joueurs)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.player_count, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerCountSpinner.setAdapter(adapter);

        // Gérer le changement de sélection dans le Spinner
        playerCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int numPlayers = position + 2; // position + 2 pour correspondre à 2 à 4 joueurs
                updatePlayerFields(numPlayers);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Bouton "Suivant" pour passer à l'écran des scores
        Button btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> {
            List<String> playerNames = new ArrayList<>();
            boolean allNamesValid = true; // Variable pour vérifier si tous les noms sont valides

            for (EditText playerNameField : playerNameFields) {
                String name = playerNameField.getText().toString().trim();
                if (!name.isEmpty()) {
                    playerNames.add(name);
                } else {
                    allNamesValid = false; // Si un champ est vide, on marque comme non valide
                }
            }

            if (allNamesValid) {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("NUM_PLAYERS", playerNames.size());
                intent.putStringArrayListExtra("PLAYER_NAMES", new ArrayList<>(playerNames));
                startActivity(intent);
            } else {
                // Afficher un message d'erreur si un des noms est vide
                Toast.makeText(MainActivity.this, "Veuillez entrer un nom pour chaque joueur.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlayerFields(int numPlayers) {
        playersLayout.removeAllViews();
        playerNameFields.clear();

        for (int i = 0; i < numPlayers; i++) {
            LinearLayout playerLayout = new LinearLayout(this);
            playerLayout.setOrientation(LinearLayout.HORIZONTAL);
            playerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView playerLabel = new TextView(this);
            playerLabel.setText("Joueur " + (i + 1) + " : ");
            playerLabel.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            playerLayout.addView(playerLabel);

            EditText playerNameField = new EditText(this);
            playerNameField.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            playerNameField.setHint("Nom du joueur " + (i + 1));
            playerNameFields.add(playerNameField);
            playerLayout.addView(playerNameField);

            playersLayout.addView(playerLayout);
        }
    }
}
