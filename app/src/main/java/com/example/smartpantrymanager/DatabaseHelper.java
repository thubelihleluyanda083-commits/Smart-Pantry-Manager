package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_PANTRY = "pantry_items";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";

    private static final String TABLE_RECIPES = "recipes";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY_DATE + " TEXT)";

        db.execSQL(createPantryTable);

        createRecipesTable(db);
        insertDefaultRecipes(db);
    }

    private void createRecipesTable(SQLiteDatabase db) {

        String createRecipesTable =
                "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "ingredients TEXT NOT NULL, " +
                        "instructions TEXT NOT NULL)";

        db.execSQL(createRecipesTable);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        createRecipesTable(db);
        insertDefaultRecipes(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        // Make sure the recipes table exists and contains default recipes.
        createRecipesTable(db);
        insertDefaultRecipes(db);
    }

    // Add an ingredient to the pantry.
    public long addPantryItem(
            String name,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);

        return db.insert(TABLE_PANTRY, null, values);
    }

    // Retrieve all pantry items.
    public Cursor getAllPantryItems() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );
    }

    // Retrieve only pantry ingredient names.
    public Cursor getPantryIngredientNames() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_PANTRY,
                new String[]{COLUMN_NAME},
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );
    }

    // Delete an ingredient.
    public int deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // Update an ingredient.
    public int updatePantryItem(
            int id,
            String name,
            double quantity,
            String unit,
            String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_EXPIRY_DATE, expiryDate);

        return db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // Add a recipe.
    public long addRecipe(
            String name,
            String ingredients,
            String instructions) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("ingredients", ingredients);
        values.put("instructions", instructions);

        return db.insert(TABLE_RECIPES, null, values);
    }

    // Retrieve all recipes.
    public Cursor getAllRecipes() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                "name ASC"
        );
    }

    // Insert one recipe into the database.
    private void insertRecipe(
            SQLiteDatabase db,
            String name,
            String ingredients,
            String instructions) {

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("ingredients", ingredients);
        values.put("instructions", instructions);

        db.insert(TABLE_RECIPES, null, values);
    }

    // Add default recipes only when the recipes table is empty.
    private void insertDefaultRecipes(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_RECIPES,
                null
        );

        int recipeCount = 0;

        try {
            if (cursor.moveToFirst()) {
                recipeCount = cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }

        if (recipeCount > 0) {
            return;
        }

        insertRecipe(db, "Cheese Sandwich",
                "bread, cheese",
                "Place cheese between two slices of bread.");

        insertRecipe(db, "Tomato Pasta",
                "pasta, tomato, onion",
                "Cook pasta. Prepare a tomato and onion sauce, then mix.");

        insertRecipe(db, "Vegetable Omelette",
                "eggs, onion, tomato",
                "Beat eggs, add chopped vegetables, and cook in a pan.");

        insertRecipe(db, "Peanut Butter Sandwich",
                "bread, peanut butter",
                "Spread peanut butter on bread and make a sandwich.");

        insertRecipe(db, "Banana Oatmeal",
                "oats, milk, banana",
                "Cook oats with milk and serve with sliced banana.");

        insertRecipe(db, "Scrambled Eggs",
                "eggs, butter, salt",
                "Beat the eggs and cook them with butter and salt in a pan.");

        insertRecipe(db, "Garlic Bread",
                "bread, butter, garlic",
                "Mix crushed garlic with butter, spread on bread, and toast.");

        insertRecipe(db, "Chicken Rice",
                "chicken, rice, onion",
                "Cook the rice. Cook the chicken with onion and serve together.");

        insertRecipe(db, "Tomato Salad",
                "tomato, onion, salt",
                "Chop the tomato and onion, mix them, and add salt.");

        insertRecipe(db, "Mashed Potatoes",
                "potatoes, butter, milk",
                "Boil potatoes, mash them, and mix with butter and milk.");

        insertRecipe(db, "Banana Smoothie",
                "banana, milk",
                "Blend the banana with milk until smooth.");

        insertRecipe(db, "French Toast",
                "bread, eggs, milk",
                "Dip bread in beaten eggs and milk, then fry until golden.");

        insertRecipe(db, "Potato Omelette",
                "eggs, potatoes, onion",
                "Cook chopped potatoes and onion, add beaten eggs, and cook.");

        insertRecipe(db, "Tuna Sandwich",
                "bread, tuna, mayonnaise",
                "Mix tuna with mayonnaise and spread it on bread.");

        insertRecipe(db, "Rice and Beans",
                "rice, beans, onion",
                "Cook the rice and beans, then serve with cooked onion.");

        insertRecipe(db, "Apple Oatmeal",
                "oats, milk, apple",
                "Cook oats with milk and serve with chopped apple.");

        insertRecipe(db, "Cheese Omelette",
                "eggs, cheese, butter",
                "Cook beaten eggs in butter and add cheese before folding.");

        insertRecipe(db, "Tomato Soup",
                "tomato, onion, water",
                "Cook chopped tomatoes and onion in water, then blend.");

        insertRecipe(db, "Peanut Banana Toast",
                "bread, peanut butter, banana",
                "Toast bread, spread peanut butter, and add sliced banana.");

        insertRecipe(db, "Chicken Sandwich",
                "bread, chicken, mayonnaise",
                "Mix cooked chicken with mayonnaise and serve on bread.");
    }
}
