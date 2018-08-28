package gssi.aq.it.afpapp;

import android.preference.ListPreference;

/**
 * Created by gianlucascoccia on 25/07/16.
 */
public class ResourceDialog {

    private String resourceName;
    private ListPreference dialog;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public ListPreference getDialog() {
        return dialog;
    }

    public void setDialog(ListPreference dialog) {
        this.dialog = dialog;
    }

    public ResourceDialog(String resourceName, ListPreference dialog) {

        this.resourceName = resourceName;
        this.dialog = dialog;
    }
}
