package com.example.a16046562.c302p09mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etLoginID, etPassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginID = (EditText) findViewById(R.id.editTextLoginID);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        btnSubmit = (Button) findViewById(R.id.buttonSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etLoginID.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter username.", Toast.LENGTH_LONG).show();

                } else if (password.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter password.", Toast.LENGTH_LONG).show();

                } else {
                    // proceed to authenticate user
                    String url = "http://10.0.2.2/C302/C302P09/doLogin.php";
                    HttpRequest request = new HttpRequest(url);

                    request.setOnHttpResponseListener(mHttpResponseListener);
                    request.setMethod("POST");
                    request.addData("username", username);
                    request.addData("password", password);

                    request.execute();
                }
            }
        });
    }
    private HttpRequest.OnHttpResponseListener mHttpResponseListener = new HttpRequest.OnHttpResponseListener() {
        @Override
        public void onResponse(String response){
            try {
                Log.i("JSON Results: ", response);

                JSONObject result = new JSONObject(response);
                Boolean authenticated = result.getBoolean("authenticated");
                if (authenticated == true){
                    String apikey = result.getString("apikey");
                    String id = result.getString("id");

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("loginID", id);
                    editor.putString("apiKey", apikey);
                    editor.commit();

                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    i.putExtra("loginId", id);
                    i.putExtra("apikey",apikey);

                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed. Please check your login credentials.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}

