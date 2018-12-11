package com.example.franklin.visualcontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.franklin.visualcontext.data.restaurant.PreferenceIngredient;


/**
 * Page that lists all the food ingrediente you can set preferences for
 */
public class PreferenceListActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restriction);

        Button pork = findViewById(R.id.button);
        pork.setOnClickListener(this); // calling onClick() method
        Button chicken =  findViewById(R.id.button4);
        chicken.setOnClickListener(this);
        Button beef = findViewById(R.id.button5);
        beef.setOnClickListener(this); // calling onClick() method
        Button seafood = findViewById(R.id.button6);
        seafood.setOnClickListener(this);
        Button Cruciferous = findViewById(R.id.button10);
        Cruciferous.setOnClickListener(this);
        Button Marrow = findViewById(R.id.button11);
        Marrow.setOnClickListener(this);
        Button Root = findViewById(R.id.button12);
        Root.setOnClickListener(this);


    }

    public void onClick(View v) {
        String message;
        switch (v.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, PreferenceLikertScaleActivity.class);
                message = PreferenceIngredient.PORK.toString();
                intent.putExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            case R.id.button4:
                intent = new Intent(this, PreferenceLikertScaleActivity.class);
                message = PreferenceIngredient.CHICKEN.toString();
                intent.putExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            case R.id.button5:
                intent = new Intent(this, PreferenceLikertScaleActivity.class);
                message = PreferenceIngredient.BEEF.toString();
                intent.putExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            case R.id.button6:
                intent = new Intent(this, PreferenceLikertScaleActivity.class);
                message = PreferenceIngredient.SEAFOOD.toString();
                intent.putExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            case R.id.button10:
                intent = new Intent(this, PreferenceLikertScaleActivity.class);
                message = PreferenceIngredient.MARROW.toString();
                intent.putExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            case R.id.button11:
                intent = new Intent(this, PreferenceLikertScaleActivity.class);
                message = PreferenceIngredient.ROOT.toString();
                intent.putExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE, message);
                startActivity(intent);
                break;
            case R.id.button12:
                intent = new Intent(this, PreferenceLikertScaleActivity.class);
                message = PreferenceIngredient.CRUCIFEROUS.toString();
                intent.putExtra(Constants.INGREDIENT_NAME_EXTRA_MESSAGE, message);
                startActivity(intent) ;
                break;
            default:
                break;
        }
    }
}
