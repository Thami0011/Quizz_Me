
package com.example.quizme;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ScoreBoard extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_score_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ListView listView = findViewById(R.id.listview);

        // Create an adapter and bind it to the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        // Add items to the list from Intent extras
        String username = getIntent().getStringExtra("username");
        int score = getIntent().getIntExtra("score", 0);

        addItemToList(username + " - Score: " + score);
    }

    private void addItemToList(String item) {
        itemList.add(item);
        adapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }
}
