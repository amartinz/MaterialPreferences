package alexander.martinz.libs.materialpreferences;

import android.content.Context;

public class MaterialPreferenceFactory {

    public static MaterialPreference createPreference(Context context,
            boolean isCard, String key, String title, String summary) {
        final MaterialPreference preference = new MaterialPreference(context);
        preference.setAsCard(isCard);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary(summary);
        return preference;
    }

    public static MaterialPreferenceCategory createPreferenceCategory(Context context,
            String key, String title) {
        final MaterialPreferenceCategory preference = new MaterialPreferenceCategory(context);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        return preference;
    }

    public static MaterialEditTextPreference createEditTextPreference(Context context,
            boolean isCard, String key, String title, String summary, String value) {
        final MaterialEditTextPreference preference = new MaterialEditTextPreference(context);
        preference.setAsCard(isCard);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary(summary);
        preference.setValue(value);
        return preference;
    }

    public static MaterialListPreference createListPreference(Context context,
            boolean isCard, String key, String title, String summary) {
        final MaterialListPreference preference = new MaterialListPreference(context);
        preference.setAsCard(isCard);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary(summary);
        return preference;
    }

    public static MaterialSwitchPreference createSwitchPreference(Context context, boolean isCard,
            String key, String title, String summary, boolean isChecked) {
        final MaterialSwitchPreference preference = new MaterialSwitchPreference(context);
        preference.setAsCard(isCard);
        preference.init(context);
        preference.setKey(key);
        preference.setTitle(title);
        preference.setSummary(summary);
        preference.setChecked(isChecked);
        return preference;
    }

}
