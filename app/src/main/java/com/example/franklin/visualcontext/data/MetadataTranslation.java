package com.example.franklin.visualcontext.data;

import android.util.JsonReader;
import android.util.JsonToken;

import com.example.franklin.visualcontext.data.nutrition.AggregateNutritionData;
import com.example.franklin.visualcontext.data.nutrition.NutritionData;
import com.example.franklin.visualcontext.data.restaurant.Menu;
import com.example.franklin.visualcontext.data.restaurant.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import lombok.NonNull;

/**|
 * All methods related to translation between persistent files and Java objects
 */
public class MetadataTranslation {
    /**
     * Reads a Menu Object from the relevant file
     */
    public static Menu readMenuFromJSON(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                //skip to to the menu item list
                String key = reader.nextName();
                if (key.equals("menu")) {
                    return new Menu(readMenuItemsFromJSON(reader));
                } else {
                    reader.skipValue();
                }
            }
        } finally {
            reader.close();
        }
        throw new IllegalStateException("Menu not found or improperly formatted in file");
    }

    /**
     * Reads a list of menu items from JSON
     */
    public static List<MenuItem> readMenuItemsFromJSON(JsonReader reader) throws IOException {
        List<MenuItem> menuItems = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            menuItems.add(readMenuItemFromJSON(reader));
        }
        reader.endArray();
        return menuItems;
    }

    /**
     * Reads a {@link MenuItem} from JSON
     */
    public static MenuItem readMenuItemFromJSON(JsonReader reader) throws IOException {
        Set<String> ingredients = new HashSet<>();
        Double price = null;
        String name = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("name")) {
                name = reader.nextString();
            } else if (key.equals("price")) {
                price = reader.nextDouble();
            } else if (key.equals("ingredients") && reader.peek() != JsonToken.NULL) {
                ingredients = readIngredientsFromJSON(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new MenuItem(ingredients, price, name);
    }

    /**
     * Reads a set of ingredients from JSON
     */
    public static Set<String> readIngredientsFromJSON(JsonReader reader) throws IOException {
        Set<String> ingredients = new HashSet<>();
        reader.beginArray();
        while (reader.hasNext()) {
            ingredients.add(reader.nextString());
        }
        reader.endArray();
        return ingredients;
    }

    /**
     * Translates from single json object representing a dish to {@link AggregateNutritionData}
     * object
     * @param object
     * @return
     */
    public static AggregateNutritionData jsonToAggregateNutritionData(@NonNull final JSONObject
                                                                              object) throws JSONException {
        String foodName = object.getString("food");
        Double calorie = object.getDouble("calorie");
        Double carb = object.getDouble("carb");
        Double sodium = object.getDouble("sodium");
        Double fats = object.getDouble("fats");
        Map<AggregateNutritionData.NutritionDataKeys, NutritionData> map = new HashMap();
        map.put(AggregateNutritionData.NutritionDataKeys.CALORIES, new NutritionData.Calories
                (calorie));
        map.put(AggregateNutritionData.NutritionDataKeys.CARBS, new NutritionData.Carbohydrates
                (carb));
        map.put(AggregateNutritionData.NutritionDataKeys.SODIUM, new NutritionData.Sodium
                (sodium));
        map.put(AggregateNutritionData.NutritionDataKeys.FATS, new NutritionData.Fat
                (fats));
        return new AggregateNutritionData(foodName, map);
    }

    /**
     * Translates the json daily report file to a list of strings displayable and readable to the
     * end user.
     * @return a list of strings, with each item in the list corresponding to a dish item's
     * nutrition display string
     */
    public static List<String> jsonToDisplayStrings(@NonNull final JSONObject object) throws JSONException {
        JSONArray jsonArray = object.optJSONArray("dishes");
        Objects.requireNonNull(jsonArray);
        List<String> displayStrings = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject curObject =  jsonArray.getJSONObject(i);
            AggregateNutritionData aggregateData = jsonToAggregateNutritionData(curObject);
            String foodName = aggregateData.getFoodName();
            String output = foodName + " has :\n";
            for (AggregateNutritionData.NutritionDataKeys key : aggregateData.getNutritionData()
                    .keySet()) {
                output += aggregateData.getNutritionData().get(key).toDisplayString();
            }
            displayStrings.add(output);
        }
        return displayStrings;
    }

    /**
     * Calculates the aggregate nutrition info for all dishes in one day and returns
     * a display string summarizing the daily nutrition report
     */
    public static String jsonToDailyAggregateDisplayString(@NonNull final JSONObject object) throws JSONException {
        JSONArray jsonArray = object.optJSONArray("dishes");
        Objects.requireNonNull(jsonArray);
        Map<AggregateNutritionData.NutritionDataKeys, NutritionData> cumulativeDailyData =
                new HashMap<>();
        cumulativeDailyData.put(AggregateNutritionData.NutritionDataKeys.CALORIES, new
                NutritionData.Calories(0.0));
        cumulativeDailyData.put(AggregateNutritionData.NutritionDataKeys.CARBS, new
                NutritionData.Carbohydrates(0.0));
        cumulativeDailyData.put(AggregateNutritionData.NutritionDataKeys.FATS, new
                NutritionData.Fat(0.0));
        cumulativeDailyData.put(AggregateNutritionData.NutritionDataKeys.SODIUM, new
                NutritionData.Sodium(0.0));
        //loop through dishes
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject curObject =  jsonArray.getJSONObject(i);

            AggregateNutritionData aggregateData = jsonToAggregateNutritionData(curObject);

            for (AggregateNutritionData.NutritionDataKeys key : aggregateData.getNutritionData()
                    .keySet()) {
                if (cumulativeDailyData.containsKey(key)) {
                    cumulativeDailyData.get(key).incrementValue(aggregateData.getNutritionData()
                            .get(key).getValue());
                }
            }
        }
        String output = "Daily Summary: ";
        for (AggregateNutritionData.NutritionDataKeys key : cumulativeDailyData.keySet()) {
            output += cumulativeDailyData.get(key).toDisplayString();
        }
        return output;
    }
}
