package com.example.qwirkleapp;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.View;

public class PhotoHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PhotoAdapter adapter;
    private ArrayList<JSONObject> photoHistory;
    private TextView textViewLastScores;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_history);

        textViewLastScores = findViewById(R.id.textViewLastScores);
        recyclerView = findViewById(R.id.recyclerViewPhotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        photoHistory = getPhotoHistoryFromPrefs();
        adapter = new PhotoAdapter(photoHistory);
        recyclerView.setAdapter(adapter);
        textViewLastScores.setVisibility(View.GONE);
    }

    private ArrayList<JSONObject> getPhotoHistoryFromPrefs() {
        Set<String> set = getSharedPreferences("photo_prefs", Context.MODE_PRIVATE)
                .getStringSet("photo_history", new HashSet<>());
        ArrayList<JSONObject> list = new ArrayList<>();
        for (String s : set) {
            try {
                list.add(new JSONObject(s));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Trier par date si besoin (ici, insertion = ordre)
        list.sort((a, b) -> a.optString("photoPath").compareTo(b.optString("photoPath")));
        return list;
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