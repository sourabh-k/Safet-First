package com.orgfree.safetyfirst;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_SELECT_PHONE_NUMBER = 1;
    int nameId,numberId;
    ProgressDialog pd;
    Context context = this;
    String name1,number1;
    String name2,number2;
    String name3,number3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBase db = new DataBase(this);
        Button image1,image2,image3;
        Cursor c= db.getData();
        int count = c.getCount();
        if(count==0)
        {
            setContentView(R.layout.activity_add_contacts);
            image1 = (Button) findViewById(R.id.button1);
            image2 = (Button) findViewById(R.id.button2);
            image3 = (Button) findViewById(R.id.button3);
            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        nameId= R.id.name1;
                        numberId = R.id.ph1;
                        startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
                    }

                }
            });
            image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
                        nameId= R.id.name2;
                        numberId = R.id.ph2;
                    }

                }
            });
            image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
                        nameId= R.id.name3;
                        numberId = R.id.ph3;
                    }

                }
            });

            Button addContacts= (Button) findViewById(R.id.addContacts);
            addContacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editText;
                    //Field One
                    editText=(EditText) findViewById(R.id.name1);
                    name1= editText.getText().toString();
                    editText =(EditText) findViewById(R.id.ph1);
                    number1 = editText.getText().toString();
                    //Field Two
                    editText=(EditText) findViewById(R.id.name2);
                    name2= editText.getText().toString();
                    editText =(EditText) findViewById(R.id.ph2);
                    number2 = editText.getText().toString();
                    //Field Three
                    editText=(EditText) findViewById(R.id.name3);
                    name3= editText.getText().toString();
                    editText =(EditText) findViewById(R.id.ph3);
                    number3 = editText.getText().toString();
                    if(name1.equals("") || number1.equals("") || name2.equals("") || number2.equals("")|| name3.equals("") || number3.equals("")){
                        Toast.makeText(getApplicationContext(),"Enter Valid Details",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        number1= newString(number1);
                        number2 = newString(number2);
                        number3 = newString(number3);
                       new PostData().execute();
                    //Toast.makeText(context,number1+number2+number3, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            mainactivity();
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                // Do something with the phone number
                EditText name_field= (EditText) findViewById(nameId);
                EditText number_field = (EditText) findViewById(numberId);
                number_field.setText(number);
                numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                name_field.setText(cursor.getString(numberIndex));


            }
        }
    }
    class PostData extends  AsyncTask<Void,Void,Void>
    {
        int flag;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Add Data To Server");
            this.flag=1;
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String u="http://safetyfirst.orgfree.com/index.php?tb1="+number1+"&tb2="+number2+"&tb3="+number3;
            try {
                URL url = new URL(u);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //HttpsURLConnection con =(HttpsURLConnection) url.openConnection();
                con.connect();
                int code = con.getResponseCode();
                Log.v("Code SHJSHVSDFJGSVDG",code+"");
                if(code==200)
                {
                    pd.dismiss();
                }
                }
            catch (Exception ex)
            {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pd.isShowing())
            {
                pd.dismiss();
                Toast.makeText(context,"Error: Connect to Internet",Toast.LENGTH_SHORT).show();
            }
            else
            {
                DataBase db = new DataBase(context);
                db.addRecord(name1,number1);
                db.addRecord(name2,number2);
                db.addRecord(name3,number3);
//                Intent intent = new Intent(context,MainActivity.class);
//                startActivity(intent);
                mainactivity();
            }

        }
    }
    public String newString(String s)
    {
        StringBuilder str= new StringBuilder();
        int len = s.length();
        for(int i=0;i<len;i++)
        {
            char c= s.charAt(i);
            if(c == ' ' || c== '+')
            {
                if(c== '+')
                    i+=2;
            }
            else
            {
                str.append(c);
            }
        }
        return  str.toString();
    }
    public void mainactivity()
    {
        setContentView(R.layout.activity_basic);
        Button help= (Button) findViewById(R.id.help);
        Button track =( Button) findViewById(R.id.track);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (context,AskHelp.class);
                startActivity(intent);
            }
        });
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,TrackLocation.class);
                startActivity(intent);
            }
        });
        //Toast.makeText(context,"Main Acti",Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.addContacts:
                Intent intent = new Intent(context,AddContacts.class);
                startActivityForResult(intent,-1);
                return true;
            case R.id.show_contacts:
                final Dialog sd = new Dialog(context);
                sd.setTitle("Current Safety Contacts");
                sd.setContentView(R.layout.show_contacts);
                DataBase db = new DataBase(context);
                Cursor c = db.getData();
                Toast.makeText(context,c.getCount()+"",Toast.LENGTH_SHORT).show();
                c.moveToFirst();
                //First fiels
                String name= c.getString(0);
                String number = c.getString(1);
                c.moveToNext();
                TextView nameView,numberView;
                nameView = (TextView) sd.findViewById(R.id.name1);
                numberView = (TextView) sd.findViewById(R.id.number1);
                nameView.setText(name);
                numberView.setText(number);
                //Second Field
                name= c.getString(0);
                number = c.getString(1);
                c.moveToNext();
                nameView = (TextView) sd.findViewById(R.id.name2);
                numberView = (TextView) sd.findViewById(R.id.number2);
                nameView.setText(name);
                numberView.setText(number);
                //Third filed
                name= c.getString(0);
                number = c.getString(1);
                nameView = (TextView) sd.findViewById(R.id.name3);
                numberView = (TextView) sd.findViewById(R.id.number3);
                nameView.setText(name);
                numberView.setText(number);
                sd.show();
                Button button = (Button) sd.findViewById(R.id.ok);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sd.dismiss();
                    }
                });
                return true;
            case R.id.settings:
                Intent intent1 =new Intent(context,Setting.class);
                startActivity(intent1);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
