package com.eip.bookshelf;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.support.v7.app.AppCompatActivity;
import com.eip.utilities.api.BookshelfApi;
import com.eip.utilities.model.SimpleResponse;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.facebook.FacebookSdk;

/**
 * Created by jolyn on 23/11/2016.
 */

public class SignUp extends AppCompatActivity
{
    private RelativeLayout _lp;

    public SignUp()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        _lp = (RelativeLayout)findViewById(R.id.RLSignUp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickSignUp(View v)
    {
        EditText name = (EditText) findViewById(R.id.SignUpFirstName);
        EditText password = (EditText) findViewById(R.id.SignUpPwd);
        EditText password2 = (EditText) findViewById(R.id.SignUpPwd2);
        EditText email = (EditText) findViewById(R.id.SignUpMail);
        Log.i("creation", email.getText().toString());
        if (!SignUp.verifyEmail(email.getText().toString())) {
            Snackbar snackbar = Snackbar.make(_lp, "L'adresse mail n'est pas valide !", Snackbar.LENGTH_LONG);
            snackbar.show();
            return ;
        }
        if (!SignUp.checkPassword(password.getText().toString(), password2.getText().toString())) {
            Snackbar snackbar = Snackbar.make(_lp, "Les mots de passe ne sont pas identiques !", Snackbar.LENGTH_LONG);
            snackbar.show();
            return ;
        }
        register(name.getText().toString(), email.getText().toString(), password.getText().toString());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run()
            {
                if (MainActivity.co) {
                    MenuItem  mi = (MenuItem)_lp.findViewById(R.id.nav_co);
                    mi.setTitle("Déconnexion");
                    //Todo: Call fragment shelf
                }
            }
        }, 3000);
    }

    private void register(String name, String email, String password)
    {
        BookshelfApi bookshelfApi = new Retrofit.Builder()
                .baseUrl(BookshelfApi.APIPath)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookshelfApi.class);
        Call<SimpleResponse> call = bookshelfApi.Register(name, password, email);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    SimpleResponse auth = response.body();
                    String success = auth.getSuccess();
                    Snackbar snackbar = Snackbar.make(_lp, "Création réussie !", Snackbar.LENGTH_LONG);
                    MainActivity.co = true;
                    snackbar.show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Snackbar snackbar = Snackbar.make(_lp, "Erreur : " + jObjError.getString("error"), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } catch (Exception e) {
                        Snackbar snackbar = Snackbar.make(_lp, "Une erreur est survenue.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t)
            {
                Snackbar snackbar = Snackbar.make(_lp, "Erreur : " + t.getMessage(), Snackbar.LENGTH_LONG);
                snackbar.show();
                t.printStackTrace();
            }
        });
    }

    private static Boolean verifyEmail(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static Boolean checkPassword(String pwd1,  String pwd2)
    {
        return pwd1.equals(pwd2);
    }

}