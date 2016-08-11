package com.orgfree.safetyfirst;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Setting extends AppCompatActivity {

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        TextView current_contact =(TextView) findViewById(R.id.current_contact);
        DataBase db = new DataBase(this);
        Cursor c = db.getData();
        if(c.getCount()>3)
        {
            c.moveToLast();
            String contact= c.getString(1);
            current_contact.setText(contact);
        }
        LinearLayout lineralayout = (LinearLayout) findViewById(R.id.change);
        lineralayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView current_contact =(TextView) findViewById(R.id.current_contact);
                final Dialog sd = new Dialog(context);
                sd.setTitle("Change Contact");
                sd.setContentView(R.layout.add_contact);
                Button change = (Button) sd.findViewById(R.id.change);
                sd.show();
                change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DataBase db = new DataBase(context);
                        Cursor c= db.getData();
                        int count = c.getCount();
                        EditText dialog_contact = (EditText) sd.findViewById(R.id.dialog_contact);
                        String s= dialog_contact.getText().toString();
                        if( count == 3 )
                        {
                            db.addRecord("Mine",s);
                        }
                        else
                        {
                            db.deleteData("Whole");
                            c.moveToFirst();
                            count =3;
                            for(int i =0; i<count ;i++)
                            {
                                db.addRecord(c.getString(0),c.getString(1));
                                c.moveToNext();
                            }
                            db.addRecord("Mine",s);
                        }
                        sd.dismiss();
                       current_contact.setText(s);
                    }
                });
                Button cancel = (Button)sd.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sd.dismiss();
                    }
                });

            }
        });
    }
}
