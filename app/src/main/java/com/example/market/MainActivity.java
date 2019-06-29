package com.example.market;

import android.content.ContentValues;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
   // String p;
    String myusername;
    String mypassword;
    String myID;
    String mynumber;
    String myemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


    }





    public static void main(String args[])
    {


    }


    public void goHome(View view)
    {

        setContentView(R.layout.home);

    }
    public void goLogin(View view) {
        setContentView(R.layout.login);

    }
    public void goRegister(View view)
    {
        setContentView(R.layout.register);

    }
    public void register(final View view)
    {
        final String username;
        final String password;
        String number;
        String email;
        EditText username2=(EditText)findViewById(R.id.username);
        EditText password2=(EditText)findViewById(R.id.password);
        EditText number2=(EditText)findViewById(R.id.cell);
        EditText email2=(EditText)findViewById(R.id.email);
        username=username2.getText().toString();
        password=password2.getText().toString();
        number=number2.getText().toString();
        email=email2.getText().toString();
        myemail=email;
        mynumber=number;
        ContentValues params =new ContentValues();
        //params.put("username","Elazar Levin");
        params.put("username",username);
        params.put("password",password);
        params.put("number",number);
        params.put("email",email);
        AsyncHTTPPost asyncHttpPost = new AsyncHTTPPost(
                "http://lamp.ms.wits.ac.za/~s1504371/makeuser.php",params) {
            @Override
            protected void onPostExecute(String output) {
                final TextView t=(TextView)findViewById(R.id.test2);
                //t.setText(output);
                if(output!="doesnt works")//change
                {

                    myusername=username;
                    mypassword=password;
                    proceed();
                }
                else
                {
                    register(view);
                }
            }
        };


        asyncHttpPost.execute();
    }
    public void login(final View view)
    {

        //add method to get users credentials
        final String username;
        final String password;
        final EditText username2=(EditText)findViewById(R.id.editText3);
        final EditText password2=(EditText)findViewById(R.id.editText5);
        final TextView t=(TextView)findViewById(R.id.test);
        t.setText("");
        username=username2.getText().toString();
        password=password2.getText().toString();
        ContentValues params =new ContentValues();
        params.put("username",username);
        AsyncHTTPPost asyncHttpPost = new AsyncHTTPPost(
                "http://lamp.ms.wits.ac.za/~s1504371/getpassword2.php",params) {
            @Override
            protected void onPostExecute(String output) {
                JSONObject item=new JSONObject();
                String username3;
                String password3;
                try {
                    JSONArray all = new JSONArray(output);

                        item=all.getJSONObject(0);


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                try {
                    output=item.getString("password");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //processJSON(output);
               // t.setText(failed);
                if(output.equals(password))
                {
                    t.setText("");
                    myusername=username;
                    mypassword=password;
                    ContentValues params =new ContentValues();
                    //params.put("username","Elazar Levin");
                    params.put("username",username);
                    AsyncHTTPPost asyncHttpPost = new AsyncHTTPPost(
                            "http://lamp.ms.wits.ac.za/~s1504371/getdetails.php",params) {
                        @Override
                        protected void onPostExecute(String output) {
                            JSONArray myArray = null;
                            try {
                                myArray = new JSONArray(output);
                                JSONObject item = myArray.getJSONObject(0);
                                mynumber=item.getString("number");
                                myemail=item.getString("email");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        }


                    };
                    asyncHttpPost.execute();
                    proceed();

                }
                else
                {

                    t.setText("Unable to login, please try again");
                    //setContentView(R.layout.home);
                    //login(view);

                }

            }
        };
        asyncHttpPost.execute();


    }
    public void proceed()
    {
        setContentView(R.layout.mainmenu);
    }

    public void ListItems(View view)
    {
       // Double aveRating;
        EditText e=findViewById(R.id.search);
        setContentView(R.layout.listitems);

        String Search =e.getText().toString();

        ContentValues params =new ContentValues();
        params.put("Search",Search);

      final AsyncHTTPPost asyncHttpPost = new AsyncHTTPPost(
                "http://lamp.ms.wits.ac.za/~s1504371/listitems.php",params) {
             @Override
             protected void onPostExecute(String output) {
                 if(output!="nothing") {
                     LinearLayout l = findViewById(R.id.listlayout);
                     try{



                         JSONArray myArray = new JSONArray(output);
                         for (int i = 0; i < myArray.length(); i++) {
                             RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.thing, null);

                             JSONObject item = myArray.getJSONObject(i);

                             final int id = item.getInt("id");
                             final String name = item.getString("name");
                             final Double price = item.getDouble("price");
                             final Double aveRating;
                             final String description=item.getString("description");
                            // ContentValues params2 =new ContentValues();
                             //params2.put("id",item.getString("id"));
                             //AsyncHTTPPost asyncHttpPost = new AsyncHTTPPost(
                             //        "http://lamp.ms.wits.ac.za/~s1504371/calcave.php",params2) {
                             //         @Override
                             //              protected void onPostExecute(String output){
                             //
                             //
                             //              }
                             //
                             // };

                             TextView t;
                             t = rl.findViewById(R.id.itemid);
                             t.setText("" + id);
                             t = rl.findViewById(R.id.Itemname);
                             t.setText(name);
                             t = rl.findViewById(R.id.Itemprice);
                             t.setText("R"+price);
                             t=rl.findViewById(R.id.Itemrating);
                             getrating(id, t);
                             //System.out.println(getrating(5));
                             rl.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                    // System.out.println(getrating(id));
                                     myID=(""+id);
                                     setContentView(R.layout.itemview);
                                     TextView txt=findViewById(R.id.itemnamedisp);
                                     txt.setText(name);
                                     txt=findViewById(R.id.descriptiondisp);
                                     txt.setText(description);
                                     txt=findViewById(R.id.pricedisp);
                                     txt.setText("R"+price);
                                     txt=findViewById(R.id.emaildisp);
                                     txt.setText(myemail);
                                     txt=findViewById(R.id.numberdisp);
                                     txt.setText("0"+mynumber);
                                     txt=findViewById(R.id.usernamedisp);
                                     txt.setText(myusername);
                                 }
                             });
                             l.addView(rl);

                         }
                     } catch (JSONException e) {
                         e.printStackTrace();


                     }
                 }
                 else
                 {
                     proceed();
                 }
             }
        };


        asyncHttpPost.execute();


    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void AddItem(final View view)
    {
        final String name;
        final String date;
        final String price;
        final String description;

        EditText name2=(EditText)findViewById(R.id.name);


        final EditText price2=(EditText)findViewById(R.id.price);
        EditText description2=(EditText)findViewById(R.id.description);
        name=name2.getText().toString();
        price=price2.getText().toString();
        description=description2.getText().toString();
        //username=username2.getText().toString();
        //password=password2.getText().toString();
        //number=number2.getText().toString();
        //email=email2.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = sdf.format(new Date());

        //date=Calendar.getInstance().getTime().toString();
        ContentValues params =new ContentValues();
        //params.put("username","Elazar Levin");
        params.put("name",name);
        params.put("date",date);
        params.put("price",price);
        params.put("description",description);
        AsyncHTTPPost asyncHttpPost = new AsyncHTTPPost(
                "http://lamp.ms.wits.ac.za/~s1504371/additem.php",params) {
            @Override
            protected void onPostExecute(String output) {
                //final TextView t=(TextView)findViewById(R.id.test2);
                //t.setText(output);
                if(output!="doesnt works")//change
                {

                    //TextView p=findViewById(R.id.textView11);
                   // p.setText("Item added successfully");
                //    myusername=username;
                //    mypassword=password;
                 //   proceed();
                    proceed();
                }
                else
                {
                    TextView p=findViewById(R.id.textView12);
                    p.setText("Error, please try again");
                    //AddItem(view);

//                    proceed();
                }
            }
        };


        asyncHttpPost.execute();

    }
    public void getrating(int ID, final TextView t)
    {
        final String[] x = new String[1];
        ContentValues params =new ContentValues();
        params.put("id",ID);
        AsyncHTTPPost asyncHttpPost = new AsyncHTTPPost(
                "http://lamp.ms.wits.ac.za/~s1504371/calcave.php",params) {
            @Override
            protected void onPostExecute(String output) {

                x[0] = output;
                t.setText(x[0]);
                //System.out.println(x[0]);

            }


        };
        asyncHttpPost.execute();



    }
    public void newitem(View view){
        setContentView(R.layout.additem);
    }
    public void gomainmenu(View view)
    {
        setContentView(R.layout.mainmenu);
    }
    public void addrating(final View v) {
        Spinner s=findViewById(R.id.spinner2);
        String S=s.getSelectedItem().toString();

        String id=myID;
        String rating=S;
        String username = myusername;
        ContentValues params = new ContentValues();
        params.put("id", id);
        params.put("rating", rating);
        params.put("username", username);
        AsyncHTTPPost asyncHttpPost = new AsyncHTTPPost(
                "http://lamp.ms.wits.ac.za/~s1504371/addrating.php", params) {
            @Override
            protected void onPostExecute(String output) {

                gomainmenu(v);
                //System.out.println(x[0]);

            }


        };
        asyncHttpPost.execute();
    }


}
