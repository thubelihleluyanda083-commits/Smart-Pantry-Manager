**Smart Pantry Manager**

**1. Project Description**

Smart Pantry Manager is an Android mobile application developed using Java in Android Studio. The application helps users manage ingredients stored in their pantry and find recipes based on the ingredients they have available.

The application allows users to:

* Add pantry ingredients.
* View saved pantry ingredients.
* Edit ingredient details.
* Delete ingredients.
* Set ingredient quantities and units.
* Add optional expiry dates.
* View suggested recipes based on available ingredients.
* View recipe ingredients and cooking instructions.
* Save settings, including expiry alert preferences and preferred units.

**2. Technologies Used**

* **Java:** Used to develop the application logic.
* **Android Studio:** Used to develop and test the Android application.
* **SQLite:** Used to store pantry ingredients and recipe information locally.
* **XML:** Used to design the application's user interfaces.
* **SharedPreferences:** Used to save user settings on the device.
* **GitHub:** Used to store and manage the project source code.

**3. Database Choice and Justification**

SQLite was selected because it is a lightweight database that works directly on an Android device. It does not require a separate database server or an internet connection to store and retrieve information.

The application uses two main database tables:

**pantry_items**

This table stores pantry ingredient information, including the ingredient ID, name, quantity, unit, and optional expiry date.

**recipes**

This table stores recipe information, including the recipe ID, name, required ingredients, and cooking instructions.

SQLite is suitable for this application because it supports local data storage and allows pantry information to remain available after the application is closed and reopened.

**4. Requirements**

To open and run this project, you need:

* Android Studio.
* The Android SDK required by the project.
* A computer that meets Android Studio's system requirements.
* An Android emulator or a compatible Android device.

**5. How to Open and Run the Application**

1. Download or clone this repository from GitHub.
2. Open Android Studio.
3. Select **Open** and choose the downloaded project folder.
4. Allow Android Studio to synchronise the Gradle files.
5. Wait for the project to finish loading.
6. Start an Android emulator or connect a compatible Android device with USB debugging enabled.
7. Click the **Run** button in Android Studio.
8. Select the emulator or connected device.
9. Wait for Smart Pantry Manager to install and open.

If Android Studio displays a Gradle synchronisation or SDK error, check that the required SDK components are installed and that the project configuration is correct.

**6. Main Application Features**

**Pantry Management**

Users can add, view, edit, and delete pantry ingredients. The application checks that required fields are entered and that the quantity is valid.

**Expiry Dates**

Users can optionally select an expiry date for an ingredient.

**Suggested Recipes**

The application compares the ingredients stored in the pantry with the ingredients required by each recipe. Recipes are suggested when all their required ingredients are available.

**Recipe Details**

Users can view the ingredients and cooking instructions for a suggested recipe.

**Settings**

Users can save their expiry alert preference and preferred unit.

**Note:** The expiry alert setting saves the user's preference but does not currently generate actual expiry notifications. The preferred unit setting is saved but does not automatically convert quantities.

**7. Project Structure**

The main Java source files include:

* 'MainActivity.java' - manages the home screen and navigation.
* 'PantryActivity.java' - manages pantry ingredients.
* 'RecipesActivity.java' - displays recipes that match available ingredients.
* 'RecipeDetailActivity.java' - displays recipe ingredients and instructions.
* 'DatabaseHelper.java'- creates and manages the SQLite database.
* 'SettingsActivity.java'- manages user preferences.

The project also contains Android resources, layout files, and configuration files.

**8. Testing**

The application was tested to check that users can navigate between screens, add and edit ingredients, delete ingredients, view suggested recipes, open recipe details, save settings, and retain pantry data after restarting the application.

Input validation was also tested to ensure that required information is entered and that invalid quantities are rejected.

**9. Author**

**Student:** Luyanda Ndlangamandla

**Student Number:** 402308991

**Qualification:** Diploma – BSc in Information Technology Articulation

**Module:** Mobile App Development 700
