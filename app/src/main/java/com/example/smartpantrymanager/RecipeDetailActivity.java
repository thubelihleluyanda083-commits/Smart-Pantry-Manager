package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        TextView tvRecipeName = findViewById(R.id.tvRecipeName);
        TextView tvRecipeIngredients = findViewById(R.id.tvRecipeIngredients);
        TextView tvRecipeInstructions = findViewById(R.id.tvRecipeInstructions);

        Intent intent = getIntent();

        String recipeName = intent.getStringExtra("recipe_name");
        String ingredients = intent.getStringExtra("recipe_ingredients");
        String instructions = intent.getStringExtra("recipe_instructions");

        if (recipeName == null || ingredients == null || instructions == null) {
            Toast.makeText(
                    this,
                    "Recipe details could not be loaded.",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        tvRecipeName.setText(recipeName);
        tvRecipeIngredients.setText(ingredients);
        tvRecipeInstructions.setText(instructions);
    }
}

