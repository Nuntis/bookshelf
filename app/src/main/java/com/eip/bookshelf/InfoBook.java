package com.eip.bookshelf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Maxime on 25/04/2017.
 */

public class InfoBook extends AppCompatActivity
{
    private ListView _lvCom;
    private ArrayList<ComAdapter> _modelListCom = new ArrayList<>();

    public InfoBook()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _lvCom = (ListView) findViewById(R.id.LVCom);
        Bundle b = getIntent().getExtras();
        HashMap<String, String> info = (HashMap<String, String>)b.getSerializable("info");

        setAdapters();
        moreDataBook(info);
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

    private void setAdapters()
    {
        customAdapterCom _adapterCom = new customAdapterCom(this, _modelListCom);
        _lvCom.setAdapter(_adapterCom);
        //Todo: Récupérer les vrais commentaires :|
        _modelListCom.add(new ComAdapter("Maxime", "23/02/2016 à 13h42", "Super livre !"));
        getTotalHeightofListView();
    }

    protected void moreDataBook(HashMap<String, String> info)
    {
        TextView tv = (TextView) findViewById(R.id.TVInfoBook);
        TextView tvt = (TextView) findViewById(R.id.TVTitreBook);
        TextView tvr = (TextView) findViewById(R.id.TVResum);
        ImageView iv = (ImageView) findViewById(R.id.IVBook);

        tvt.setText(info.get("title"));
        tv.setText("Date de sortie : " + info.get("date"));
        tv.setText(tv.getText() + "\nAuteur : " + info.get("author"));
        tv.setText(tv.getText() + "\nGenre : " + info.get("genre"));
        tv.setText(tv.getText() + "\nNote : " + info.get("note").toString());
        tvr.setText(info.get("resume"));
        if (info.get("picture") != null && info.get("picture") != "") {
            Picasso.with(this).load(info.get("picture")).fit().into(iv);
        }
    }

    public void getTotalHeightofListView()
    {

        ListAdapter mAdapter = _lvCom.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++)
        {
            View mView = mAdapter.getView(i, null, _lvCom);

            mView.measure( View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = _lvCom.getLayoutParams();
        params.height = totalHeight + (_lvCom.getDividerHeight() * (mAdapter.getCount() - 1));
        _lvCom.setLayoutParams(params);
        _lvCom.requestLayout();

    }

    public void onClickSendCom(View v)
    {
        EditText et = (EditText) findViewById(R.id.ETCom);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH'h'mm", Locale.FRANCE);
        String currentDateandTime = sdf.format(new Date());
        _modelListCom.add(new ComAdapter("Nicolas", currentDateandTime, et.getText().toString()));
        et.setText("");
        MainActivity.hideSoftKeyboard(InfoBook.this);
        getTotalHeightofListView();
    }
}