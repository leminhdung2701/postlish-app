package com.example.application.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.R;
import com.example.application.activity.AddPost;
import com.example.application.fragment.PublishFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PublishActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    TextView createpost;
    String text=null;
    public static  final String THEME="THEME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.nav_publish);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_library:
                        startActivity(new Intent(getApplicationContext(),LibraryActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_search:
                        startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_publish:
                        break;
                    case R.id.nav_menu:
                        startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });

        //
        createpost = findViewById(R.id.create_post);
        spinner=findViewById(R.id.spinner);

        // set event
            //
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        //

        createpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text != null) {
                    Intent intent = new Intent(PublishActivity.this, AddPost.class);
                    intent.putExtra(THEME, text);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(PublishActivity.this, "Hãy chọn chủ đề cho bài viết.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Bạn hãy chọn chủ đề cho bài viết.", Toast.LENGTH_SHORT).show();
    }
}