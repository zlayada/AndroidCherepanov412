package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {

    private String[] keyTitle = {"title", "subtitle"};
    private String baseText;
    private String keyString = "keyString";

    private ListView list;
    private BaseAdapter listContentAdapter;
    private SwipeRefreshLayout refresh;
    private SharedPreferences preferences;
    private List<Map<String, String>> simpleAdapterContent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = findViewById(R.id.list);
        refresh = findViewById(R.id.textUpdate);

        preferences = getPreferences(MODE_PRIVATE);

        baseText = preferences.getString(keyString, null);
        if (baseText == null) {
            baseText = getString(R.string.large_text);
            preferences.edit().putString(keyString, baseText).apply();
        }

        listContentAdapter = createAdapter();
        list.setAdapter(listContentAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                simpleAdapterContent.remove(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareContent(keyTitle);
                listContentAdapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }
        });

    }

    @NonNull
    private BaseAdapter createAdapter() {

        prepareContent(keyTitle);
        return new SimpleAdapter(this, simpleAdapterContent, R.layout.activity_title, keyTitle,
        new int[]{R.id.textTitle, R.id.textTitleDown});
    }

    private void prepareContent(String[] keysTitle) {

        String[] titles = baseText.split("\n\n");
        simpleAdapterContent.clear();

        for (String title : titles) {
            Map<String, String> item = new HashMap<>();
            item.put(keysTitle[0], title);
            item.put(keysTitle[1], Integer.toString(title.length()));
            simpleAdapterContent.add(item);
        }
    }
}
