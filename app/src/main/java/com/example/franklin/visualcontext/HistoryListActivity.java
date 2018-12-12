package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.franklin.visualcontext.data.MetadataTranslation;
import com.example.franklin.visualcontext.data.restaurant.PreferenceIngredient;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HistoryListActivity extends AppCompatActivity implements View.OnClickListener{

    ListView list;
    ArrayList<String> date_list, date_list_only;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        Button btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(this);
        list = (ListView) findViewById(R.id.historylist);
        date_list = new ArrayList<String>();
        date_list_only = new ArrayList<String>();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();

        File filesDir = getApplicationContext().getFilesDir();
        File[] files = filesDir.listFiles();
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        for (File file : files) {
            if (file.getName().contains("20")) {
                try (FileInputStream in = new FileInputStream(file)) {
                    String jsonString = IOUtils.toString(in);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.optJSONArray("dishes");
                    String date = file.getName().replace(".json", "");

                    long total_calorie = 0;
                    long total_carb = 0;
                    long total_sodium = 0;
                    long total_fats = 0;

                    String food;
                    long calorie, carb, sodium, fats;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject row = jsonArray.getJSONObject(i);
                        food = row.getString("food");
                        calorie = row.getLong("calorie");
                        carb = row.getLong("carb");
                        sodium = row.getLong("sodium");
                        fats = row.getLong("fats");
                        total_calorie = total_calorie + calorie;
                        total_carb = total_carb + carb;
                        total_sodium = total_sodium + sodium;
                        total_fats = total_fats + fats;
                    }

                    date_list_only.add(date);
                    //date = date + " " + MetadataTranslation.jsonToDailyAggregateDisplayString(jsonObject);

                    date = date + " total " + total_calorie + " calories " + total_carb + " g carb " + total_sodium + " mg sodium " + total_fats + " g fats";
                    date_list.add(date);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, date_list);

        list.setAdapter(itemsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(HistoryListActivity.this, HistoryActivity.class);
                intent.putExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE, date_list_only.get(position));
                startActivity(intent);

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button3:

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }}
