package com.example.franklin.visualcontext;

import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteToReport {

    public static void WriteToJsonReport(File file, String calorie, String carb, String sodium,
                                         String fats, String
                                                 food) {
        if (file.length() == 0) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                JSONObject jsonObj = new JSONObject();
                JSONObject foodObject = new JSONObject();
                //populate preferences with default preference levels
                foodObject.put("food", food);
                foodObject.put("sodium", sodium);
                foodObject.put("calorie", calorie);
                foodObject.put("fats", fats);
                foodObject.put("carb", carb);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(foodObject);
                jsonObj.put("dishes", jsonArray);
                out.write(jsonObj.toString().getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("MYAPP", "unexpected JSON exception", e);
            }
        } else {
            try (FileInputStream in = new FileInputStream(file)) {
                String jsonString = IOUtils.toString(in);
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject new_jsonObject = new JSONObject();
                JSONArray jsonArray = jsonObject.optJSONArray("dishes");
                JSONObject foodObject = new JSONObject();
                foodObject.put("food", food);
                foodObject.put("sodium", sodium);
                foodObject.put("calorie", calorie);
                foodObject.put("fats", fats);
                foodObject.put("carb", carb);
                jsonArray.put(foodObject);
                new_jsonObject.put("dishes", jsonArray);
                FileOutputStream out = new FileOutputStream(file);
                out.write(new_jsonObject.toString().getBytes());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e("MYAPP", "unexpected JSON exception", e);
            }

        }
    }
}
