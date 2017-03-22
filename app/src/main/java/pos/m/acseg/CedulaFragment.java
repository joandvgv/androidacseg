package pos.m.acseg;

/**
 * Created by Slaush on 21/03/2017.
 */


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Slaush on 21/03/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CedulaFragment extends Fragment {

    Button btn;
    EditText cedula;
    TextView result;
    ImageView isOnView;
    ImageView isNotView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_cedula, container, false);
        btn = (Button) view.findViewById(R.id.button);
        cedula = (EditText) view.findViewById(R.id.editText);
        result = (TextView) view.findViewById(R.id.textView4);
        isOnView = (ImageView)view.findViewById(R.id.imageView3);
        isNotView = (ImageView)view.findViewById(R.id.imageView4);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String url = "https://acseg.herokuapp.com/api/persona/"+cedula.getText().toString();

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response)
                            {
                                if(response.has("data"))
                                {
                                    JSONObject jObj = null;
                                    String name = "";
                                    try {
                                        jObj = response.getJSONObject("data");
                                        System.out.println(jObj);
                                        name = jObj.getString("nombre");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    result.setText(name);
                                    isNotView.setVisibility(View.INVISIBLE);
                                    isOnView.setVisibility(View.VISIBLE);
                                }
                                else {
                                    isNotView.setVisibility(View.VISIBLE);
                                    isOnView.setVisibility(View.INVISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                RequestManager.getInstance(getContext()).addToRequestQueue(jsObjRequest);
            }
        });
        return view;
    }


}


