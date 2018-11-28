package com.example.franklin.visualcontext.data.restaurant;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import static com.example.franklin.visualcontext.Constants.JSON_PREFERENCES_KEY;
import static com.example.franklin.visualcontext.Constants.JSON_RESTRICTIONS_KEY;

/**
 * Consists of the items persistently stored for a user's food preferences
 */
@Getter
@AllArgsConstructor
public class PreferenceMetadata {

    private final List<PreferenceIngredient> preferenceIngredients;

    private final List<DietRestrictions> dietRestrictions;

    /**
     * Factory method that converts a json file to a PreferenceMetadata java object
     */
    public static PreferenceMetadata createFromJson(@NonNull final InputStream in) throws
            IOException,
            JSONException {
        String jsonStr = IOUtils.toString(in);
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject prefsObject = jsonObject.getJSONObject(JSON_PREFERENCES_KEY);
        List<PreferenceIngredient> preferenceIngredients = new ArrayList<>();
        for (PreferenceIngredient ingredient : PreferenceIngredient.values()) {
            String ingredientName = ingredient.toString();
            PreferenceLevels prefLevel = PreferenceLevels.valueOf(prefsObject.getString(ingredientName));
            ingredient.setPrefLevel(prefLevel);
            preferenceIngredients.add(ingredient);
        }

        List<DietRestrictions> dietRestrictions = new ArrayList<>();
        JSONArray restrictionsArray = jsonObject.getJSONArray(JSON_RESTRICTIONS_KEY);
        for (int i = 0; i < restrictionsArray.length(); ++i) {
            dietRestrictions.add(DietRestrictions.valueOf(restrictionsArray.getString(i)));
        }
        return new PreferenceMetadata(preferenceIngredients, dietRestrictions);
    }
}
