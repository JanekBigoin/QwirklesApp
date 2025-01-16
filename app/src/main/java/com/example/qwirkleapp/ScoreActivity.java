package com.example.qwirkleapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkleapp.Player;
import com.example.qwirkleapp.PlayerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private List<Player> players;  // Liste des joueurs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // Récupérer les informations sur le nombre de joueurs et leurs noms
        Intent intent = getIntent();
        int numPlayers = intent.getIntExtra("NUM_PLAYERS", 0); // Récupérer le nombre de joueurs
        List<String> playerNames = intent.getStringArrayListExtra("PLAYER_NAMES"); // Récupérer les noms des joueurs

        // Initialiser la liste des joueurs à partir des noms
        players = new ArrayList<>();
        for (String name : playerNames) {
            players.add(new Player(name));  // Créer un joueur avec le nom donné
        }

        // Configurer la RecyclerView
        RecyclerView recyclerView = findViewById(R.id.playersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Créer l'adaptateur et le lier à la RecyclerView
        PlayerAdapter adapter = new PlayerAdapter(players);
        recyclerView.setAdapter(adapter);

        // Bouton de réinitialisation des scores
        Button btnReset = findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(v -> {
            // Créer l'alerte pour confirmer la réinitialisation
            new AlertDialog.Builder(ScoreActivity.this)
                    .setTitle("Réinitialiser les scores")
                    .setMessage("Êtes-vous sûr de vouloir réinitialiser les scores de tous les joueurs ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (Player player : players) {
                                player.setScore(0);  // Réinitialiser les scores
                            }
                            adapter.notifyDataSetChanged();  // Mettre à jour la RecyclerView
                            Toast.makeText(ScoreActivity.this, "Scores réinitialisés !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Non", null)  // Si l'utilisateur appuie sur "Non", rien ne se passe
                    .show();
        });
    }
}
