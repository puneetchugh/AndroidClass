package com.example.puneet.accessphonebook;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    private Button btn = null;
    private TextView txt = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button1);
        txt = (TextView) findViewById(R.id.textView1);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == btn) {
            try {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error in intent : ", e.toString());
            }
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cur = managedQuery(contactData, null, null, null, null);
                ContentResolver contect_resolver = getContentResolver();

                if (cur.moveToFirst()) {
                    String id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String name = "";
                    String no = "";

                    Cursor phoneCur = contect_resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    if (phoneCur.moveToFirst()) {
                        name = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        no = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }

                    Log.e("Phone no & name :***: ", name + " : " + no);
                    txt.append(name + " : " + no + "\n");

                    id = null;
                    name = null;
                    no = null;
                    phoneCur = null;
                }
                contect_resolver = null;
                cur = null;
                //                      populateContacts();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e("IllegalArgumentExcp", e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error :: ", e.toString());
        }
    }
}