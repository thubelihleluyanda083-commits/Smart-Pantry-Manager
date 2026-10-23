package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class RecipesActivity extends AppCompatActivity {

    private final ArrayList<String> recipeNames = new ArrayList<>();
    private final ArrayList<String> recipeIngredients = new ArrayList<>();
    private final ArrayList<String> recipeInstructions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        TextView tvRecipesMessage =
                findViewById(R.id.tvRecipesMessage);

        ListView listSuggestedRecipes =
                findViewById(R.id.listSuggestedRecipes);

        HashSet<String> pantryIngredients = new HashSet<>();

        // Load pantry ingredient names.
        try (Cursor pantryCursor =
                     databaseHelper.getPantryIngredientNames()) {

            if (pantryCursor != null && pantryCursor.moveToFirst()) {
                do {
                    String ingredient = pantryCursor.getString(0);

                    if (ingredient != null) {
                        pantryIngredients.add(
                                normaliseIngredient(ingredient)
                        );
                    }

                } while (pantryCursor.moveToNext());
            }
        }

        int totalRecipes = 0;

        // Read saved recipes from the database.
        try (Cursor recipeCursor = databaseHelper.getAllRecipes()) {

            if (recipeCursor != null && recipeCursor.moveToFirst()) {
                do {
                    totalRecipes++;

                    String name = recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow("name")
                    );

                    String ingredients = recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow("ingredients")
                    );

                    String instructions = recipeCursor.getString(
                            recipeCursor.getColumnIndexOrThrow("instructions")
                    );

                    if (name == null
                            || ingredients == null
                            || ingredients.trim().isEmpty()) {
                        continue;
                    }

                    String[] requiredIngredients = ingredients.split(",");

                    boolean hasAllIngredients = true;

                    for (String required : requiredIngredients) {

                        String normalisedRequired =
                                normaliseIngredient(required);

                        if (normalisedRequired.isEmpty()
                                || !pantryIngredients.contains(
                                normalisedRequired
                        )) {

                            hasAllIngredients = false;
                            break;
                        }
                    }

                    // Only show recipes when every required ingredient exists.
                    if (hasAllIngredients) {
                        recipeNames.add(name);
                        recipeIngredients.add(ingredients);
                        recipeInstructions.add(
                                instructions == null ? "" : instructions
                        );
                    }

                } while (recipeCursor.moveToNext());
            }
        }

        // Show feedback when no recipe matches.
        if (recipeNames.isEmpty()) {

            tvRecipesMessage.setText(
                    totalRecipes == 0
                            ? "No recipes are saved in the database."
                            : pantryIngredients.isEmpty()
                            ? "Add ingredients to your pantry to find recipes."
                            : getString(R.string.no_matching_recipes)
            );

        } else {

            tvRecipesMessage.setText(
                    "Tap a recipe to view its ingredients and instructions."
            );
        }

        // Display matching recipes.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                recipeNames
        );

        listSuggestedRecipes.setAdapter(adapter);

        // Open the details page when a recipe is selected.
        listSuggestedRecipes.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Intent intent = new Intent(
                            RecipesActivity.this,
                            RecipeDetailActivity.class
                    );

                    intent.putExtra(
                            "recipe_name",
                            recipeNames.get(position)
                    );

                    intent.putExtra(
                            "recipe_ingredients",
                            recipeIngredients.get(position)
                    );

                    intent.putExtra(
                            "recipe_instructions",
                            recipeInstructions.get(position)
                    );

                    startActivity(intent);
                }
        );

        // Handle screen insets.
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }

    // Standardise ingredient names before comparing them.
    private String normaliseIngredient(String ingredient) {

        if (ingredient == null) {
            return "";
        }

        return ingredient.trim()
                .toLowerCase(Locale.ROOT);
    }
}