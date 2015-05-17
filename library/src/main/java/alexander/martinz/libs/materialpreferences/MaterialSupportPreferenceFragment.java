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

package alexander.martinz.libs.materialpreferences;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MaterialSupportPreferenceFragment extends Fragment {
    protected LinearLayout mFragmentPreferenceContainer;

    protected int getLayoutResourceId() {
        return -1;
    }

    protected String getUnknown() {
        return "---";
    }

    public MaterialSupportPreferenceFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.material_prefs_fragment_preference, container, false);
        mFragmentPreferenceContainer =
                (LinearLayout) rootView.findViewById(R.id.fragment_preference_container);

        int layoutResId = getLayoutResourceId();
        if (layoutResId != -1) {
            View child = inflater.inflate(layoutResId, mFragmentPreferenceContainer, false);
            mFragmentPreferenceContainer.addView(child);
        }

        return rootView;
    }

    @NonNull public LinearLayout getPreferenceContainer() {
        return mFragmentPreferenceContainer;
    }

    public void addPreference(View preference) {
        mFragmentPreferenceContainer.addView(preference);
    }

    public MaterialPreference createPreference(boolean isCard, String key, String title,
            String summary) {
        return MaterialPreferenceFactory.createPreference(getActivity(), isCard, key, title,
                summary);
    }

    public MaterialPreferenceCategory createPreferenceCategory(String key, String title) {
        return MaterialPreferenceFactory.createPreferenceCategory(getActivity(), key, title);
    }

    public MaterialSwitchPreference createSwitchPreference(boolean isCard, String key, String title,
            String summary, boolean isChecked) {
        return MaterialPreferenceFactory.createSwitchPreference(getActivity(), isCard, key, title,
                summary, isChecked);
    }

    public MaterialEditTextPreference createEditTextPreference(boolean isCard, String key,
            String title, String summary, String value) {
        return MaterialPreferenceFactory.createEditTextPreference(getActivity(), isCard, key, title,
                summary, value);
    }

    public MaterialListPreference createListPreference(boolean isCard, String key,
            String title, String summary) {
        return MaterialPreferenceFactory.createListPreference(getActivity(), isCard, key, title,
                summary);
    }

}
