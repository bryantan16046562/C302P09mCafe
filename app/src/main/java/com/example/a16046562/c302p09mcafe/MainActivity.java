package com.example.a16046562.c302p09mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ListView lvCategories;
    private ArrayList<MenuCategory> alCategories;
    private ArrayAdapter<MenuCategory> aaCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCategories = (ListView) findViewById(R.id.listViewCategories);
        alCategories = new ArrayList<MenuCategory>();
        aaCategories = new ArrayAdapter<MenuCategory>(this, android.R.layout.simple_list_item_1, alCategories);
        lvCategories.setAdapter(aaCategories);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loginID = prefs.getString("loginid", "");
        String apiKey = prefs.getString("apikey", "");

        if (loginID.equalsIgnoreCase("") || apiKey.equalsIgnoreCase("")) {
            // redirect back to login screen
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        HttpRequest request = new HttpRequest("http://10.0.2.2/C302/C302P09/getMenuCategories.php");
        request.setOnHttpResponseListener(mHttpResponseListener);
        request.setMethod("POST");
        request.addData("loginid", loginID);
        request.addData("apikey", apiKey);
        request.execute();


        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuCategory cat = alCategories.get(position); //get selected item
                Intent intent = new Intent(MainActivity.this, DisplayMenuItemsActivity.class);
                intent.putExtra("MenuCategory", cat);
                startActivityForResult(intent, 1);

            }
        });
    }
    private HttpRequest.OnHttpResponseListener mHttpResponseListener = new HttpRequest.OnHttpResponseListener() {
        @Override
        public void onResponse(String response){
            try {
                Log.i("JSON Results: ", response);

                JSONArray results = new JSONArray(response);
                for (int a=0; a<results.length();a++){
                    JSONObject record = results.getJSONObject(a);
                    MenuCategory cat = new MenuCategory(record.getString("menu_item_category_id"),record.getString("menu_item_category_description"));
                    alCategories.add(cat);
                }
                aaCategories.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public  boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                startActivity(new Intent(this,LoginActivity.class));
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
