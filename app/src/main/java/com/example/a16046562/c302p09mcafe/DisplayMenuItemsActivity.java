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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayMenuItemsActivity extends AppCompatActivity {
    private ListView lvMenuItems;
    private ArrayList<MenuItem> alMenuItems;
    private ArrayAdapter<MenuItem> aaMenuItems;

    private MenuCategory cat;
    private String categoryId;
    private MenuItem itemss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu_items);

        lvMenuItems = (ListView) findViewById(R.id.listViewMenuItems);
        alMenuItems = new ArrayList<MenuItem>();
        aaMenuItems = new ArrayAdapter<MenuItem>(this, android.R.layout.simple_list_item_1, alMenuItems);
        lvMenuItems.setAdapter(aaMenuItems);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loginID = prefs.getString("loginid", "");
        String apiKey = prefs.getString("apikey", "");

        Intent intent = getIntent();
        cat = (MenuCategory) intent.getSerializableExtra("MenuCategory");
        categoryId = cat.getCategoryId();

        HttpRequest request = new HttpRequest("http://10.0.2.2/C302/C302P09/getMenuItemsByCategory.php");
        request.setOnHttpResponseListener(mHttpResponseListener);
        request.setMethod("POST");
        request.addData("loginid", loginID);
        request.addData("apikey", apiKey);
        request.execute();
    }
    private HttpRequest.OnHttpResponseListener mHttpResponseListener = new HttpRequest.OnHttpResponseListener() {
        @Override
        public void onResponse(String response){
            try {
                Log.i("JSON Results: ", response);

                JSONArray results = new JSONArray(response);
                for (int a=0; a<results.length();a++){
                    JSONObject record = results.getJSONObject(a);
                    String itemId = record.getString("menu_item_id");
                    String categoryId = record.getString("menu_item_category_id");
                    String description = record.getString("menu_item_description");
                    String unitPrice = record.getString("menu_item_unit_price");

                    itemss = new MenuItem(itemId, categoryId, description, unitPrice);
                    itemss = new MenuItem();
                    alMenuItems.add(itemss);
                }
                aaMenuItems.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public  boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.addmenuitem:
                Intent intent = new Intent(this, AddMenuItemActivity.class);
                intent.putExtra("categoryId",categoryId);
                intent.putExtra("MenuCategory",cat);
                startActivity(intent);
                return true;
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
