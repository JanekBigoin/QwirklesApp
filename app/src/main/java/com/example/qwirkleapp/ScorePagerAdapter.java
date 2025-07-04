package com.example.qwirkleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ScorePagerAdapter extends RecyclerView.Adapter<ScorePagerAdapter.ScoreViewHolder> {
    private List<Player> players;

    public ScorePagerAdapter(List<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_joueur_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Player player = players.get(position);
        holder.textViewName.setText(player.getName());
        holder.textViewScore.setText(String.valueOf(player.getScore()));

        Button btnPlus1 = holder.btnPlus1;
        Button btnPlus5 = holder.btnPlus5;
        Button btnPlus12 = holder.btnPlus12;
        Button btnMinus1 = holder.btnMinus1;
        Button btnMinus5 = holder.btnMinus5;
        Button btnMinus12 = holder.btnMinus12;
        TextView textViewLastPoint = holder.textViewLastPoint;

        View.OnClickListener showLastPoint = v -> {
            textViewLastPoint.setVisibility(View.VISIBLE);
            textViewLastPoint.setAlpha(1f);
            textViewLastPoint.animate().alpha(0f).setDuration(1200).withEndAction(() -> textViewLastPoint.setVisibility(View.INVISIBLE)).start();
        };

        btnPlus1.setOnClickListener(v -> {
            player.setScore(player.getScore() + 1);
            holder.textViewScore.setText(String.valueOf(player.getScore()));
            textViewLastPoint.setText("+1");
            textViewLastPoint.setTextColor(v.getContext().getResources().getColor(R.color.button_positive));
            showLastPoint.onClick(v);
        });
        btnPlus5.setOnClickListener(v -> {
            player.setScore(player.getScore() + 5);
            holder.textViewScore.setText(String.valueOf(player.getScore()));
            textViewLastPoint.setText("+5");
            textViewLastPoint.setTextColor(v.getContext().getResources().getColor(R.color.button_positive));
            showLastPoint.onClick(v);
        });
        btnPlus12.setOnClickListener(v -> {
            player.setScore(player.getScore() + 12);
            holder.textViewScore.setText(String.valueOf(player.getScore()));
            textViewLastPoint.setText("+12");
            textViewLastPoint.setTextColor(v.getContext().getResources().getColor(R.color.button_positive));
            showLastPoint.onClick(v);
        });
        btnMinus1.setOnClickListener(v -> {
            if (player.getScore() > 0) player.setScore(player.getScore() - 1);
            holder.textViewScore.setText(String.valueOf(player.getScore()));
            textViewLastPoint.setText("-1");
            textViewLastPoint.setTextColor(v.getContext().getResources().getColor(R.color.button_negative));
            showLastPoint.onClick(v);
        });
        btnMinus5.setOnClickListener(v -> {
            if (player.getScore() >= 5) player.setScore(player.getScore() - 5);
            else player.setScore(0);
            holder.textViewScore.setText(String.valueOf(player.getScore()));
            textViewLastPoint.setText("-5");
            textViewLastPoint.setTextColor(v.getContext().getResources().getColor(R.color.button_negative));
            showLastPoint.onClick(v);
        });
        btnMinus12.setOnClickListener(v -> {
            if (player.getScore() >= 12) player.setScore(player.getScore() - 12);
            else player.setScore(0);
            holder.textViewScore.setText(String.valueOf(player.getScore()));
            textViewLastPoint.setText("-12");
            textViewLastPoint.setTextColor(v.getContext().getResources().getColor(R.color.button_negative));
            showLastPoint.onClick(v);
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewScore, textViewLastPoint;
        Button btnPlus1, btnPlus5, btnPlus12, btnMinus1, btnMinus5, btnMinus12;
        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewPlayerName);
            textViewScore = itemView.findViewById(R.id.textViewPlayerScore);
            btnPlus1 = itemView.findViewById(R.id.btnPlus1);
            btnPlus5 = itemView.findViewById(R.id.btnPlus5);
            btnPlus12 = itemView.findViewById(R.id.btnPlus12);
            btnMinus1 = itemView.findViewById(R.id.btnMinus1);
            btnMinus5 = itemView.findViewById(R.id.btnMinus5);
            btnMinus12 = itemView.findViewById(R.id.btnMinus12);
            textViewLastPoint = itemView.findViewById(R.id.textViewLastPoint);
        }
    }
} 