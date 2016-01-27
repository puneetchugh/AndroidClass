package com.example.puneet.learnrestapi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button saveButton ;
    EditText areaName ;
    EditText restaurantCategory ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = (Button) findViewById(R.id.click_button);
        areaName = (EditText) findViewById(R.id.type_area_id);
        restaurantCategory = (EditText) findViewById(R.id.type_restaurant_category);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void save(View view){

        String area = areaName.getText().toString();
        String restCat = restaurantCategory.getText().toString();

        String toBeSentToApi = area + "::" + restCat;
        Intent fourSquareApi = new Intent(Intent.ACTION_VIEW);
        fourSquareApi.setData(Uri.parse("location://com.example.nyu.hw3api"));
                fourSquareApi.putExtra("searchVal", toBeSentToApi);
        startActivityForResult(fourSquareApi, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == 0)
    }
}
