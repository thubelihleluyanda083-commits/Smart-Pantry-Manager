package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PantryActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private EditText etIngredientName;
    private EditText etIngredientQuantity;
    private EditText etIngredientUnit;
    private EditText etIngredientExpiryDate;

    private Button btnAddIngredient;
    private ListView listPantryItems;

    private PantryAdapter pantryAdapter;

    private final ArrayList<String> pantryItems = new ArrayList<>();
    private final ArrayList<Integer> pantryItemIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantry);

        databaseHelper = new DatabaseHelper(this);

        View mainView = findViewById(R.id.main);

        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(
                    mainView,
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

        etIngredientName = findViewById(R.id.etIngredientName);
        etIngredientQuantity = findViewById(R.id.etIngredientQuantity);
        etIngredientUnit = findViewById(R.id.etIngredientUnit);
        etIngredientExpiryDate =
                findViewById(R.id.etIngredientExpiryDate);

        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        listPantryItems = findViewById(R.id.listPantryItems);

        pantryAdapter = new PantryAdapter(this, pantryItems);
        listPantryItems.setAdapter(pantryAdapter);

        etIngredientExpiryDate.setInputType(InputType.TYPE_NULL);
        etIngredientExpiryDate.setFocusable(false);
        etIngredientExpiryDate.setClickable(true);

        etIngredientExpiryDate.setOnClickListener(
                view -> showDatePicker(etIngredientExpiryDate)
        );

        // Long-press an ingredient to manage it.
        listPantryItems.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    Toast.makeText(
                            PantryActivity.this,
                            "Ingredient selected",
                            Toast.LENGTH_SHORT
                    ).show();

                    if (position < 0
                            || position >= pantryItemIds.size()
                            || position >= pantryItems.size()) {
                        return true;
                    }

                    int ingredientId = pantryItemIds.get(position);
                    String ingredientName = pantryItems.get(position);

                    showManageIngredientDialog(
                            ingredientId,
                            ingredientName
                    );

                    return true;
                }
        );

        btnAddIngredient.setOnClickListener(
                view -> addIngredient()
        );

        loadPantryItems();
    }

    private void showManageIngredientDialog(
            int ingredientId,
            String ingredientName
    ) {
        new AlertDialog.Builder(this)
                .setTitle("Manage Ingredient")
                .setMessage(
                        "What would you like to do with:\n"
                                + ingredientName + "?"
                )
                .setPositiveButton(
                        "Edit",
                        (dialog, which) -> showEditDialog(ingredientId)
                )
                .setNegativeButton(
                        "Delete",
                        (dialog, which) -> confirmDelete(ingredientId)
                )
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void addIngredient() {

        String name = etIngredientName.getText().toString().trim();
        String quantityText =
                etIngredientQuantity.getText().toString().trim();
        String unit = etIngredientUnit.getText().toString().trim();
        String expiryDate =
                etIngredientExpiryDate.getText().toString().trim();

        if (name.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) {
            Toast.makeText(
                    this,
                    R.string.fill_all_fields,
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (expiryDate.isEmpty()) {
            expiryDate = null;
        }

        try {
            double quantity = Double.parseDouble(quantityText);

            if (!isValidQuantity(quantity)) {
                Toast.makeText(
                        this,
                        R.string.invalid_quantity,
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            long result = databaseHelper.addPantryItem(
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            if (result != -1) {
                loadPantryItems();

                etIngredientName.setText("");
                etIngredientQuantity.setText("");
                etIngredientUnit.setText("");
                etIngredientExpiryDate.setText("");

                Toast.makeText(
                        this,
                        "Ingredient added successfully.",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                Toast.makeText(
                        this,
                        R.string.ingredient_save_failed,
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    R.string.invalid_quantity,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private boolean isValidQuantity(double quantity) {
        return quantity > 0
                && !Double.isInfinite(quantity)
                && !Double.isNaN(quantity);
    }

    private void showDatePicker(EditText dateField) {

        Calendar calendar = Calendar.getInstance();
        String currentDate = dateField.getText().toString().trim();

        if (!currentDate.isEmpty()) {
            try {
                String[] parts = currentDate.split("-");

                if (parts.length == 3) {
                    calendar.set(
                            Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]) - 1,
                            Integer.parseInt(parts[2])
                    );
                }
            } catch (NumberFormatException ignored) {
                // Keep today's date if the existing date is invalid.
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker picker, int year, int month, int day) -> {
                    String selectedDate = String.format(
                            Locale.ROOT,
                            "%04d-%02d-%02d",
                            year,
                            month + 1,
                            day
                    );

                    dateField.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.setButton(
                DatePickerDialog.BUTTON_NEUTRAL,
                "Clear",
                (dialog, which) -> dateField.setText("")
        );

        datePickerDialog.show();
    }

    private void showEditDialog(int ingredientId) {

        Cursor cursor = databaseHelper.getAllPantryItems();

        String currentName = "";
        String currentQuantity = "";
        String currentUnit = "";
        String currentExpiryDate = "";

        if (cursor != null) {
            try {
                int idColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_ID
                );
                int nameColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_NAME
                );
                int quantityColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_QUANTITY
                );
                int unitColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_UNIT
                );
                int expiryColumn = cursor.getColumnIndex(
                        DatabaseHelper.COLUMN_EXPIRY_DATE
                );

                while (cursor.moveToNext()) {
                    if (cursor.getInt(idColumn) == ingredientId) {
                        currentName = cursor.getString(nameColumn);

                        currentQuantity = String.valueOf(
                                cursor.getDouble(quantityColumn)
                        );

                        currentUnit = cursor.getString(unitColumn);

                        if (expiryColumn >= 0) {
                            String savedDate = cursor.getString(expiryColumn);

                            if (savedDate != null) {
                                currentExpiryDate = savedDate;
                            }
                        }

                        break;
                    }
                }
            } finally {
                cursor.close();
            }
        }

        if (currentName.isEmpty()) {
            Toast.makeText(
                    this,
                    "Ingredient not found.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        EditText nameInput = new EditText(this);
        nameInput.setHint("Ingredient name");
        nameInput.setSingleLine(true);
        nameInput.setText(currentName);

        EditText quantityInput = new EditText(this);
        quantityInput.setHint("Quantity");
        quantityInput.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );
        quantityInput.setText(currentQuantity);

        EditText unitInput = new EditText(this);
        unitInput.setHint("Unit (e.g. cups, pieces)");
        unitInput.setSingleLine(true);
        unitInput.setText(currentUnit);

        EditText expiryDateInput = new EditText(this);
        expiryDateInput.setHint("Expiry date (optional)");
        expiryDateInput.setFocusable(false);
        expiryDateInput.setClickable(true);
        expiryDateInput.setInputType(InputType.TYPE_NULL);
        expiryDateInput.setText(currentExpiryDate);

        expiryDateInput.setOnClickListener(
                view -> showDatePicker(expiryDateInput)
        );

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        int padding = (int) (
                20 * getResources().getDisplayMetrics().density
        );

        layout.setPadding(padding, padding, padding, 0);
        layout.addView(nameInput);
        layout.addView(quantityInput);
        layout.addView(unitInput);
        layout.addView(expiryDateInput);

        new AlertDialog.Builder(this)
                .setTitle("Edit Ingredient")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) -> {

                    String newName =
                            nameInput.getText().toString().trim();
                    String quantityText =
                            quantityInput.getText().toString().trim();
                    String newUnit =
                            unitInput.getText().toString().trim();
                    String newExpiryDate =
                            expiryDateInput.getText().toString().trim();

                    if (newName.isEmpty()
                            || quantityText.isEmpty()
                            || newUnit.isEmpty()) {
                        Toast.makeText(
                                PantryActivity.this,
                                R.string.fill_all_fields,
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    if (newExpiryDate.isEmpty()) {
                        newExpiryDate = null;
                    }

                    try {
                        double newQuantity =
                                Double.parseDouble(quantityText);

                        if (!isValidQuantity(newQuantity)) {
                            Toast.makeText(
                                    PantryActivity.this,
                                    R.string.invalid_quantity,
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        int result = databaseHelper.updatePantryItem(
                                ingredientId,
                                newName,
                                newQuantity,
                                newUnit,
                                newExpiryDate
                        );

                        if (result > 0) {
                            loadPantryItems();

                            Toast.makeText(
                                    PantryActivity.this,
                                    "Ingredient updated successfully.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            Toast.makeText(
                                    PantryActivity.this,
                                    "Could not update ingredient.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    } catch (NumberFormatException e) {
                        Toast.makeText(
                                PantryActivity.this,
                                R.string.invalid_quantity,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmDelete(int ingredientId) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage(
                        "Are you sure you want to delete this ingredient?"
                )
                .setPositiveButton("Yes", (dialog, which) -> {

                    int result = databaseHelper.deletePantryItem(
                            ingredientId
                    );

                    if (result > 0) {
                        loadPantryItems();

                        Toast.makeText(
                                PantryActivity.this,
                                "Ingredient deleted successfully.",
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        Toast.makeText(
                                PantryActivity.this,
                                "Could not delete ingredient.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void loadPantryItems() {

        Cursor cursor = databaseHelper.getAllPantryItems();

        pantryItems.clear();
        pantryItemIds.clear();

        if (cursor != null) {
            try {
                int idColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_ID
                );
                int nameColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_NAME
                );
                int quantityColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_QUANTITY
                );
                int unitColumn = cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_UNIT
                );
                int expiryColumn = cursor.getColumnIndex(
                        DatabaseHelper.COLUMN_EXPIRY_DATE
                );

                while (cursor.moveToNext()) {

                    int id = cursor.getInt(idColumn);
                    String name = cursor.getString(nameColumn);
                    double quantity = cursor.getDouble(quantityColumn);
                    String unit = cursor.getString(unitColumn);

                    String expiryDate = null;

                    if (expiryColumn >= 0) {
                        expiryDate = cursor.getString(expiryColumn);
                    }

                    String itemDisplay = name
                            + " - " + quantity
                            + " " + unit;

                    if (expiryDate != null
                            && !expiryDate.trim().isEmpty()) {
                        itemDisplay += "\nExpiry date: " + expiryDate;
                    }

                    pantryItemIds.add(id);
                    pantryItems.add(itemDisplay);
                }

            } finally {
                cursor.close();
            }
        }

        pantryAdapter.notifyDataSetChanged();
    }
}