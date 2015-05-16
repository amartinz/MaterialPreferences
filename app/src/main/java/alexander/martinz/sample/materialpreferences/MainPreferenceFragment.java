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

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import alexander.martinz.libs.materialpreferences.MaterialEditTextPreference;
import alexander.martinz.libs.materialpreferences.MaterialPreference;
import alexander.martinz.libs.materialpreferences.MaterialSupportPreferenceFragment;
import alexander.martinz.libs.materialpreferences.MaterialSwitchPreference;

public class MainPreferenceFragment extends MaterialSupportPreferenceFragment implements MaterialPreference.MaterialPreferenceChangeListener {
    private static final String KEY_EDITTEXT_DUMMY_ONE = "edittext_dummy_one";
    private static final String KEY_SWITCH_DUMMY_ONE = "switch_dummy_one";
    private static final String KEY_SWITCH_DUMMY_TWO = "switch_dummy_two";

    private Toast mToast;

    public MainPreferenceFragment() { }

    @Override public int getLayoutResourceId() {
        return R.layout.preferences_main;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialEditTextPreference dummyEditTextOne =
                (MaterialEditTextPreference) view.findViewById(R.id.edittext_dummy_one);
        dummyEditTextOne.setOnPreferenceChangeListener(this);

        MaterialSwitchPreference dummySwitchOne =
                (MaterialSwitchPreference) view.findViewById(R.id.switch_dummy_one);
        dummySwitchOne.setOnPreferenceChangeListener(this);

        MaterialSwitchPreference dummySwitchTwo =
                (MaterialSwitchPreference) view.findViewById(R.id.switch_dummy_two);
        dummySwitchTwo.setOnPreferenceChangeListener(this);
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

    private void makeToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }

}
