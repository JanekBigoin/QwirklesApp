package com.example.qwirkleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkleapp.R;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<Player> players;

    public PlayerAdapter(List<Player> players) {
        this.players = players;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.playerName.setText(player.getName());
        holder.playerScore.setText("Score: " + player.getScore()); // Ajouter "Score" avant le score

        // Réinitialiser l'historique à chaque action
        holder.scoreHistoryLayout.removeAllViews();

        // Ajouter +1 au score
        holder.btnAdd.setOnClickListener(v -> {
            player.addScore(1);
            player.getScoreHistory().clear(); // Effacer l'historique avant d'ajouter la nouvelle action
            player.getScoreHistory().add("+1");
            notifyDataSetChanged();
        });

        // Enlever -1 du score
        holder.btnSubtract.setOnClickListener(v -> {
            player.addScore(-1);
            player.getScoreHistory().clear(); // Effacer l'historique avant d'ajouter la nouvelle action
            player.getScoreHistory().add("-1");
            notifyDataSetChanged();
        });

        // Ajouter +5 au score
        holder.btnAdd5.setOnClickListener(v -> {
            player.addScore(5);
            player.getScoreHistory().clear(); // Effacer l'historique avant d'ajouter la nouvelle action
            player.getScoreHistory().add("+5");
            notifyDataSetChanged();
        });

        // Ajouter +12 au score
        holder.btnAdd12.setOnClickListener(v -> {
            player.addScore(12);
            player.getScoreHistory().clear(); // Effacer l'historique avant d'ajouter la nouvelle action
            player.getScoreHistory().add("+12");
            notifyDataSetChanged();
        });

        // Enlever -5 du score
        holder.btnSubtract5.setOnClickListener(v -> {
            player.addScore(-5);
            player.getScoreHistory().clear(); // Effacer l'historique avant d'ajouter la nouvelle action
            player.getScoreHistory().add("-5");
            notifyDataSetChanged();
        });

        // Enlever -12 du score
        holder.btnSubtract12.setOnClickListener(v -> {
            player.addScore(-12);
            player.getScoreHistory().clear(); // Effacer l'historique avant d'ajouter la nouvelle action
            player.getScoreHistory().add("-12");
            notifyDataSetChanged();
        });

        // Mettre à jour l'historique des scores
        holder.updateScoreHistory(player);

        // Appliquer la couleur verte aux boutons positifs et rouge aux boutons négatifs
        holder.btnAdd.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.holo_green_dark));  // Vert pour +1
        holder.btnAdd5.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.holo_green_dark));  // Vert pour +5
        holder.btnAdd12.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.holo_green_dark));  // Vert pour +12

        holder.btnSubtract.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.holo_red_dark));  // Rouge pour -1
        holder.btnSubtract5.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.holo_red_dark));  // Rouge pour -5
        holder.btnSubtract12.setBackgroundColor(holder.itemView.getResources().getColor(android.R.color.holo_red_dark));  // Rouge pour -12
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {

        TextView playerName;
        TextView playerScore;
        Button btnAdd;
        Button btnSubtract;
        Button btnAdd5;
        Button btnAdd12;
        Button btnSubtract5;
        Button btnSubtract12;
        LinearLayout scoreHistoryLayout; // Layout pour afficher l'historique

        public PlayerViewHolder(View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.playerName);
            playerScore = itemView.findViewById(R.id.playerScore);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnSubtract = itemView.findViewById(R.id.btnSubtract);
            btnAdd5 = itemView.findViewById(R.id.btnAdd5);
            btnAdd12 = itemView.findViewById(R.id.btnAdd12);
            btnSubtract5 = itemView.findViewById(R.id.btnSubtract5);
            btnSubtract12 = itemView.findViewById(R.id.btnSubtract12);
            scoreHistoryLayout = itemView.findViewById(R.id.scoreHistoryLayout); // Récupérer le layout de l'historique
        }

        // Méthode pour mettre à jour l'historique des scores
        public void updateScoreHistory(Player player) {
            // Ajouter chaque élément de l'historique dans le layout
            for (String scoreChange : player.getScoreHistory()) {
                TextView scoreHistoryText = new TextView(itemView.getContext());
                scoreHistoryText.setText(scoreChange);
                if (scoreChange.contains("-")) {
                    scoreHistoryText.setTextColor(itemView.getResources().getColor(android.R.color.holo_red_dark)); // Rouge pour les soustractions
                } else {
                    scoreHistoryText.setTextColor(itemView.getResources().getColor(android.R.color.holo_green_dark)); // Vert pour les ajouts
                }
                scoreHistoryLayout.addView(scoreHistoryText);
            }
        }
    }
}
