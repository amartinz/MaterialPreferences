/*
 * Copyright 2015 Alexander Martinz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package alexander.martinz.sample.materialpreferences;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import alexander.martinz.libs.materialpreferences.MaterialEditTextPreference;
import alexander.martinz.libs.materialpreferences.MaterialListPreference;
import alexander.martinz.libs.materialpreferences.MaterialPreference;
import alexander.martinz.libs.materialpreferences.MaterialPreferenceCategory;
import alexander.martinz.libs.materialpreferences.MaterialSupportPreferenceFragment;
import alexander.martinz.libs.materialpreferences.MaterialSwitchPreference;

public class MainPreferenceFragment extends MaterialSupportPreferenceFragment implements MaterialPreference.MaterialPreferenceChangeListener, MaterialPreference.MaterialPreferenceClickListener {
    private static final String KEY_EDITTEXT_DUMMY_ONE = "edittext_dummy_one";
    private static final String KEY_LIST_DUMMY_ONE = "list_dummy_one";
    private static final String KEY_SWITCH_DUMMY_ONE = "switch_dummy_one";
    private static final String KEY_SWITCH_DUMMY_TWO = "switch_dummy_two";

    private Toast mToast;

    public MainPreferenceFragment() { }

    @Override public int getLayoutResourceId() {
        return R.layout.preferences_main;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupPreferenceChangeListenerDemo(view);
        setupDynamicCategoryDemo(view);
        setupGenerateDemo();
    }

    private void setupPreferenceChangeListenerDemo(View view) {
        final MaterialEditTextPreference dummyEditTextOne =
                (MaterialEditTextPreference) view.findViewById(R.id.edittext_dummy_one);
        dummyEditTextOne.setOnPreferenceChangeListener(this);

        final MaterialListPreference dummyListOne =
                (MaterialListPreference) view.findViewById(R.id.list_dummy_one);
        dummyListOne.setOnPreferenceChangeListener(this);

        final MaterialSwitchPreference dummySwitchOne =
                (MaterialSwitchPreference) view.findViewById(R.id.switch_dummy_one);
        dummySwitchOne.setOnPreferenceChangeListener(this);

        final MaterialSwitchPreference dummySwitchTwo =
                (MaterialSwitchPreference) view.findViewById(R.id.switch_dummy_two);
        dummySwitchTwo.setOnPreferenceChangeListener(this);
        // TODO: implement attrs support
        dummySwitchTwo.setSelectable(false);
    }

    private void setupDynamicCategoryDemo(View view) {
        final MaterialPreferenceCategory dynamicCategory =
                (MaterialPreferenceCategory) view.findViewById(R.id.category_dynamic);

        addPreferenceToCategory(dynamicCategory, "dummy1",
                "Dynamic material_prefs_preference 1", "I got dynamically added");

        addPreferenceToCategory(dynamicCategory, "dummy2",
                "Dynamic material_prefs_preference 2", "Click me!")
                .setOnPreferenceClickListener(this);

        addPreferenceToCategory(dynamicCategory, "dummy3",
                "Dynamic adsfasfd 3", "You may touch me as well")
                .setOnPreferenceClickListener(this);
    }

    private void setupGenerateDemo() {
        final MaterialPreference generatedOne = createPreference(false,
                "generated1", "Generated 1", "i am generated and dont do much");
        addPreference(generatedOne);

        final MaterialPreference generatedTwo = createPreference(true,
                "generated2", "Generated 2", "at least i look like a card...");
        addPreference(generatedTwo);

        final MaterialSwitchPreference generatedSwitchOne = createSwitchPreference(false,
                "generatedSwitch1", "Generated switch 1", "i am generated", false);
        addPreference(generatedSwitchOne);

        final MaterialSwitchPreference generatedSwitchTwo = createSwitchPreference(false,
                "generatedSwitch2", "Generated switch 2", "i am generated AND checked", true);
        addPreference(generatedSwitchTwo);

        final MaterialSwitchPreference generatedSwitchThree = createSwitchPreference(true,
                "generatedSwitch3", "Generated switch 3", "i am generated AND checked AND a card",
                true);
        addPreference(generatedSwitchThree);

        final MaterialEditTextPreference generatedEditTextOne = createEditTextPreference(false,
                "generatedEditText1", "Generated EditText 1", "Isn\'t this awesome?!", "no way");
        addPreference(generatedEditTextOne);

        final MaterialListPreference generatedListOne = createListPreference(true,
                "generatedList1", "Generated List 1", "What is the point in this?");
        addPreference(generatedListOne);

        final MaterialListPreference generatedListTwo = createListPreference(false,
                "generatedList2", "Generated List 2", "What is my favorite drink?");
        generatedListTwo.setAdapter(generatedListTwo.createAdapter(
                new CharSequence[]{ "Coffee", "Chocolate", "Blood" },
                new CharSequence[]{ "1", "2", "3" }
        ));
        generatedListTwo.setOnPreferenceChangeListener(this);
        addPreference(generatedListTwo);

        final MaterialPreferenceCategory generatedCategoryOne = createPreferenceCategory(
                "GeneratedCategory1", "Generated category 1");
        addPreference(generatedCategoryOne);
        generatedCategoryOne.addPreference(createPreference(false, "cat1", "Mep", "mepmep"));
        generatedCategoryOne.addPreference(createPreference(false, "cat2", "Derp", "derpderpderp"));
        generatedCategoryOne.addPreference(createSwitchPreference(false, "s1", "Herp", null, true));
    }

    private MaterialPreference addPreferenceToCategory(final MaterialPreferenceCategory category,
            final String key, final String title, final String summary) {
        final Context context = getActivity();
        final MaterialPreference preference = new MaterialPreference(context);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary(TextUtils.isEmpty(summary) ? "---" : summary);
        category.addPreference(preference);
        return preference;
    }

    @Override public boolean onPreferenceChanged(MaterialPreference preference, Object newValue) {
        final boolean handled;
        final String key = preference.getKey();
        final String value = String.valueOf(newValue);
        switch (key) {
            case KEY_EDITTEXT_DUMMY_ONE: {
                handled = true;
                break;
            }
            case KEY_LIST_DUMMY_ONE: {
                handled = true;
                break;
            }
            case KEY_SWITCH_DUMMY_ONE: {
                handled = true;
                break;
            }
            case KEY_SWITCH_DUMMY_TWO: {
                handled = true;
                break;
            }
            default: {
                handled = false;
                break;
            }
        }
        makeToast(String.format("%s -> %s | handled -> %s", key, value, handled));

        return handled;
    }

    @Override public boolean onPreferenceClicked(MaterialPreference preference) {
        final String key = preference.getKey();
        final String title = preference.getTitle();

        makeToast(String.format("key -> %s | title -> %s", key, title));

        return true;
    }

    private void makeToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

}
