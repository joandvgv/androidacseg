package pos.m.acseg;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextWatcher;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MonitoreoFragment extends Fragment {

    ListView listView;
    ArrayList<JSONObject> list;
    MonitoreoAdapter adapter;
    EditText txtCed;
    TextView hoy;
    TextView mes;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_monitoreo, container, false);
        listView = (ListView)view.findViewById(R.id.items);
        txtCed = (EditText) view.findViewById(R.id.editText2);
        list = new ArrayList<>();
        TextView title = (TextView) view.findViewById(R.id.item_header);
        hoy = (TextView) view.findViewById(R.id.hoy);
        mes = (TextView) view.findViewById(R.id.mes);
        title.setText("Monitoreo Personas");
        adapter = new MonitoreoAdapter(list,getContext());
        listView.setAdapter(adapter);
        txtCed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        insertarDatos();
        cargarHoy();
        cargarMes();
        return view;
    }


    private void insertarDatos()
    {

        String url = "https://acseg.herokuapp.com/api/logs/persona";


        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>()
                {

                    @Override
                    public void onResponse(JSONArray response)
                    {

                        for (int i = 0; i< response.length(); i++)
                        {
                            try {
                                list.add(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

// Access the RequestQueue through your singleton class.
        RequestManager.getInstance(getContext()).addToRequestQueue(jsObjRequest);

    }



    private void cargarHoy()
    {
        String url = "https://acseg.herokuapp.com/api/logs/hour";

        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        hoy.setText(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private void cargarMes()
    {
        String url = "https://acseg.herokuapp.com/api/logs/month";

        StringRequest stringRequest = new StringRequest
                (Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        mes.setText(response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestManager.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}


class MonitoreoAdapter extends ArrayAdapter<JSONObject> implements Filterable {
    private ArrayList<JSONObject> objs;
    ArrayList<JSONObject> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    // View lookup cache
    private static class ViewHolder {
        TextView txtCI;
        TextView txtFecha;
        TextView txtOP;

    }

    public Filter getFilter() {
        return mFilter;
    }


    public int getCount() {
        return filteredData.size();
    }

    public JSONObject getItem(int position) {
        return filteredData.get(position);
    }


    MonitoreoAdapter(ArrayList<JSONObject> list, Context context)
    {
        super(context, R.layout.item_monitoreo, list);
        objs = list;
        filteredData = list ;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_monitoreo, parent, false);
            viewHolder.txtCI = (TextView) convertView.findViewById(R.id.cedula);
            viewHolder.txtFecha = (TextView) convertView.findViewById(R.id.fecha);
            viewHolder.txtOP = (TextView) convertView.findViewById(R.id.operacion);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        String fecha = data.optString("day") + " - " +
                data.optString("month") + " - "+
                data.optString("year");
        viewHolder.txtCI.setText(data.optString("CI"));
        viewHolder.txtFecha.setText(fecha);
        viewHolder.txtOP.setText(data.optString("op"));

        return convertView;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final ArrayList<JSONObject> list = objs;

            int count = list.size();
            final ArrayList<JSONObject> nlist = new ArrayList<>(count);

            JSONObject object;

            for (int i = 0; i < count; i++)
            {
                System.out.println("getting objects");
                object = list.get(i);
                String cedula = object.optString("CI");

                if (cedula.toLowerCase().contains(filterString)) {
                    System.out.println("MATCHED CI");
                    nlist.add(object);
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            filteredData = (ArrayList<JSONObject>) results.values;
            System.out.println("publish results");
            System.out.println(filteredData);
            notifyDataSetChanged();
        }

    }

}
