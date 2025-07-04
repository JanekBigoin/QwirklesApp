package com.example.qwirkleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

        // Adapter pour le Spinner (nombre de joueurs)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.player_count, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerCountSpinner.setAdapter(adapter);

        // Gestion du changement de sélection dans le Spinner
        playerCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int numPlayers = position + 2; // 2 à 4 joueurs
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
            boolean allNamesValid = true;

            for (EditText playerNameField : playerNameFields) {
                String name = playerNameField.getText().toString().trim();
                if (!name.isEmpty()) {
                    playerNames.add(name);
                } else {
                    allNamesValid = false;
                }
            }

            if (allNamesValid) {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("NUM_PLAYERS", playerNames.size());
                intent.putStringArrayListExtra("PLAYER_NAMES", new ArrayList<>(playerNames));
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Veuillez entrer un nom pour chaque joueur.", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnPhotoHistory = findViewById(R.id.btn_photo_history);
        btnPhotoHistory.setText("Photos des parties précédentes");
        btnPhotoHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PhotoHistoryActivity.class);
            startActivity(intent);
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
            playerNameField.setSingleLine(true); // Bloquer les sauts de ligne
            playerNameField.setImeOptions(EditorInfo.IME_ACTION_NEXT); // Permet de passer au champ suivant
            playerNameField.setInputType(android.text.InputType.TYPE_CLASS_TEXT);

            // Gestion du passage au champ suivant avec Entrée
            int currentIndex = i;
            playerNameField.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (currentIndex + 1 < playerNameFields.size()) {
                        playerNameFields.get(currentIndex + 1).requestFocus();
                    }
                    return true; // Bloque le comportement par défaut
                } else if (currentIndex == playerNameFields.size() - 1) {
                    List<String> playerNames = new ArrayList<>();
                    boolean allNamesValid = true;

                    for (EditText player : playerNameFields) {  // Utilise un autre nom pour éviter la collision
                        String name = player.getText().toString().trim();
                        if (!name.isEmpty()) {
                            playerNames.add(name);
                        } else {
                            allNamesValid = false;
                        }
                    }

                    if (allNamesValid) {
                        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                        intent.putExtra("NUM_PLAYERS", playerNames.size());
                        intent.putStringArrayListExtra("PLAYER_NAMES", new ArrayList<>(playerNames));
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Veuillez entrer un nom pour chaque joueur.", Toast.LENGTH_SHORT).show();
                    }
                    return true; // Bloque l'action par défaut
                }
                return false;
            });

            playerNameFields.add(playerNameField);
            playerLayout.addView(playerNameField);
            playersLayout.addView(playerLayout);
        }
    }
}
