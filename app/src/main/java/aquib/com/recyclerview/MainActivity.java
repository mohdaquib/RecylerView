package aquib.com.recyclerview;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    public static List<FeedItem> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getData("http://javatechig.com/api/get_category_posts/?dev=1&slug=android");
    }

    private void getData(String url){

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        items = new ArrayList<>();
        // Getting data from URL using Volley Library
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialog.dismiss();
                Log.v("Response", response.toString());

                JSONArray array = response.optJSONArray("posts");
                for (int i = 0; i < array.length(); i++){
                    JSONObject object = array.optJSONObject(i);
                    Gson gson = new Gson();
                    FeedItem item = gson.fromJson(object.toString(), FeedItem.class);
                    items.add(item);
                }

                adapter = new MyRecyclerAdapter(MainActivity.this, items);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        // Sort Feed Items By date
        if (id == R.id.action_sort) {
            sortByDate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sortByDate() {
        Comparator<FeedItem> comparator = new Comparator<FeedItem>() {
            @Override
            public int compare(FeedItem item1, FeedItem item2) {
                return item1.getDate().compareToIgnoreCase(item2.getDate());
            }
        };

        Collections.sort(items, comparator);

        adapter = new MyRecyclerAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }
}
