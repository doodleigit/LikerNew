package com.liker.android.DirectShare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.liker.android.R;


public class SelectContactActivity extends AppCompatActivity {
    /**
     * The action string for Intents.
     */
    public static final String ACTION_SELECT_CONTACT
            = "com.example.android.directshare.intent.action.SELECT_CONTACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        Intent intent = getIntent();
        if (!ACTION_SELECT_CONTACT.equals(intent.getAction())) {
            finish();
            return;
        }
        // Set up the list of contacts
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(mOnItemClickListener);
    }

    private final ListAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return Contact.CONTACTS.length;
        }

        @Override
        public Object getItem(int i) {
            return Contact.byId(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact, parent,
                        false);
            }
            TextView textView = (TextView) view;
            Contact contact = (Contact) getItem(i);
            ContactViewBinder.bind(contact, textView);
            return textView;
        }
    };

    private final AdapterView.OnItemClickListener mOnItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent data = new Intent();
            data.putExtra(Contact.ID, i);
            setResult(RESULT_OK, data);
            finish();
        }
    };

}
