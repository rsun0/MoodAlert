package io.raysun.moodalert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * A custom adapter for viewing friends.
 * @author Ray Sun
 */
public class FriendAdapter extends ArrayAdapter<String> {

    /**
     * Constructor.
     * @param context The context
     * @param resource The resource
     */
    public FriendAdapter(Context context, int resource) {
        super(context, resource);
    }

    /**
     * Get a view of the data set.
     * @param position The position
     * @param v The old view to reuse
     * @param parent The parent of the view
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.friend, null);
        }

        String friendName = getItem(position);

        TextView state = (TextView) v.findViewById(R.id.textView_friendName);
        state.setText(friendName);

        return v;
    }
}
