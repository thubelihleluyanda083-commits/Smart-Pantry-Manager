## Smart Pantry Manager - Testing Report

## 1. Introduction

This document describes the tests performed on the Smart Pantry Manager Android application. The purpose was to check whether the main features work correctly.

## 2. Testing Results

| Test                | Action                                                 | Expected Result                                | Result |
| ------------------- | ------------------------------------------------------ | ---------------------------------------------- | ------ |
| Home screen         | Open the application                                   | The home screen appears                        | Passed |
| Navigation          | Open the Pantry, Recipes, and Settings screens         | The selected screen opens                      | Passed |
| Add ingredient      | Enter valid ingredient details and save                | The ingredient is saved and displayed          | Passed |
| Input validation    | Try to save an ingredient without required information | Invalid input is rejected                      | Passed |
| Quantity validation | Enter an invalid quantity                              | The application rejects the quantity           | Passed |
| Edit ingredient     | Change an existing ingredient                          | The updated details are displayed              | Passed |
| Delete ingredient   | Delete an ingredient                                   | The ingredient is removed from the list        | Passed |
| Expiry date         | Select an expiry date for an ingredient                | The selected date is saved                     | Passed |
| Recipe suggestions  | Add ingredients that match a recipe                    | Matching recipes are displayed                 | Passed |
| No matching recipes | Use ingredients that do not match any recipe           | No matching recipes are displayed              | Passed |
| Recipe details      | Open a suggested recipe                                | Its ingredients and instructions are displayed | Passed |
| Settings            | Change and save settings                               | The selected preferences are saved             | Passed |
| Data persistence    | Close and reopen the application                       | Previously saved pantry data remains available | Passed |

## 3. Testing Limitations

The expiry alert setting saves the user's preference but does not currently send actual expiry notifications. The preferred unit setting is saved but does not automatically convert quantities.

Recipe suggestions are based on ingredient names. The application does not check whether the available quantities are sufficient to prepare a recipe.

## 4. Conclusion

The main application features were tested, including navigation, pantry management, input validation, recipe suggestions, recipe details, settings, and database persistence. The tests showed that the main features worked as expected during testing.
