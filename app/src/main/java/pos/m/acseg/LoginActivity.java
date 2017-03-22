package pos.m.acseg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login_button;
    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            login(user,pass);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username_edit);
        password = (EditText) findViewById(R.id.password_edit);
        login_button = (Button) findViewById(R.id.login_btn);

        login_button.setOnClickListener(loginClickListener);
    }


    private void login(String user, String pass)
    {
        JSONObject loginJSON = generarLoginJSON(user,pass);
        String url = "https://acseg.herokuapp.com/api/authuser/";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, loginJSON, new Response.Listener<JSONObject>()
                {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response.optBoolean("authsuccess"))
                        {
                            Toast.makeText(LoginActivity.this,"Bienvenido a ACSEG",Toast.LENGTH_LONG).show();
                            loggedIn();
                        }
                        else
                            Toast.makeText(LoginActivity.this,"Usuario o contraseña no válidos",Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(LoginActivity.this,"Error de autenticación",Toast.LENGTH_LONG).show();
                    }
                });

// Access the RequestQueue through your singleton class.
        RequestManager.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    private void loggedIn()
    {
        Intent i = new Intent(this,LoggedInActivity.class);
        startActivity(i);
    }

    private JSONObject generarLoginJSON(String user, String pass)
    {
        JSONObject json = new JSONObject();

        try {
            json.put("username",user);
            json.put("password",pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
