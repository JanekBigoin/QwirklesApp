package com.example.qwirkleapp;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<JSONObject> photoHistory;

    public PhotoAdapter(List<JSONObject> photoHistory) {
        this.photoHistory = photoHistory;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        JSONObject obj = photoHistory.get(position);
        String path = obj.optString("photoPath");
        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(path));
        holder.textViewScores.setText(getPlayersSummary(obj));
    }

    @Override
    public int getItemCount() {
        return photoHistory.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewScores;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPhoto);
            textViewScores = itemView.findViewById(R.id.textViewPhotoScores);
        }
    }

    private String getPlayersSummary(JSONObject obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("Joueurs : ");
        try {
            JSONArray arr = obj.getJSONArray("players");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject p = arr.getJSONObject(i);
                sb.append(p.getString("name")).append(" (").append(p.getInt("score")).append(")");
                if (i < arr.length() - 1) sb.append(", ");
            }
        } catch (JSONException e) {
            return "";
        }
        return sb.toString();
    }
} 