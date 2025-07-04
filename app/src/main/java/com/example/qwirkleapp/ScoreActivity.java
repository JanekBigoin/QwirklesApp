package com.example.qwirkleapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.qwirkleapp.Player;
import com.example.qwirkleapp.ScorePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScoreActivity extends AppCompatActivity {

    private List<Player> players;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private String currentPhotoPath;
    private ViewPager2 viewPager;
    private ScorePagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // Récupérer les informations sur le nombre de joueurs et leurs noms
        Intent intent = getIntent();
        int numPlayers = intent.getIntExtra("NUM_PLAYERS", 0);
        List<String> playerNames = intent.getStringArrayListExtra("PLAYER_NAMES");

        // Initialiser la liste des joueurs à partir des noms
        players = new ArrayList<>();
        for (String name : playerNames) {
            players.add(new Player(name));
        }

        viewPager = findViewById(R.id.viewPagerPlayers);
        pagerAdapter = new ScorePagerAdapter(players);
        viewPager.setAdapter(pagerAdapter);

        Button btnReset = findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(v -> {
            new AlertDialog.Builder(ScoreActivity.this)
                    .setTitle("Réinitialiser les scores")
                    .setMessage("Êtes-vous sûr de vouloir réinitialiser les scores de tous les joueurs ?")
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (Player player : players) {
                                player.setScore(0);
                            }
                            pagerAdapter.notifyDataSetChanged();
                            Toast.makeText(ScoreActivity.this, "Scores réinitialisés !", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });

        Button btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Erreur lors de la création du fichier photo", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Qwirkle_" + timeStamp + "_";
        File storageDir = getFilesDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            savePhotoPathToPrefs(currentPhotoPath);
            Toast.makeText(this, "Photo sauvegardée dans l'application !", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePhotoPathToPrefs(String path) {
        Context context = getApplicationContext();
        Set<String> set = new HashSet<>(context.getSharedPreferences("photo_prefs", Context.MODE_PRIVATE)
                .getStringSet("photo_history", new HashSet<>()));
        JSONObject obj = new JSONObject();
        try {
            obj.put("photoPath", path);
            JSONArray playersArray = new JSONArray();
            for (Player p : players) {
                JSONObject playerObj = new JSONObject();
                playerObj.put("name", p.getName());
                playerObj.put("score", p.getScore());
                playersArray.put(playerObj);
            }
            obj.put("players", playersArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        set.add(obj.toString());
        context.getSharedPreferences("photo_prefs", Context.MODE_PRIVATE)
                .edit().putStringSet("photo_history", set).apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Permission caméra refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
