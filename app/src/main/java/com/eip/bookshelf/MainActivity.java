package com.eip.bookshelf;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private RelativeLayout _lmain;
    private MenuItem _itm;
    private GridView _gvBiblio;
    private ListView _lvAutor;
    private ListView _lvCom;
    private customAdapterCom _adapterCom;
    private customAdapterAuthor _adapterAutor;
    private ArrayList<ComAdapter> _modelListCom = new ArrayList<>();
    private ArrayList<String> _modelListAutor = new ArrayList<>();
    private int _currentViewID;
    private int _previousViewID;
    public static boolean co = false;
    private boolean _scan = false;
    private SignIn _connect;
    private Shelf _shelf;
    public static MenuItem MenuItemCo;
    enum shelfType {
        MAINSHELF,
        PROPOSHELF,
        WISHSHELF
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set the fragment initially
        Disconnected fragment = new Disconnected();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     /**/
//        _lvAutor = (ListView) findViewById(R.id.LVAutor);
//        _lvCom = (ListView) findViewById(R.id.LVCom);
//        _gvBiblio = (GridView) findViewById(R.id.GVBiblio);
//        _lmain = (RelativeLayout) findViewById(R.id.RLMain);
        /**/
//        setAdapters();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

//        _currentViewID = R.id.RLBiblio;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.IAddBook) {
            _scan = true;
            Snackbar snackbar = Snackbar.make(_lmain, "Le livre a été ajouté à votre bibliothèque", Snackbar.LENGTH_LONG);
            snackbar.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        _itm = item;
//        verifyUser(id);

        switch (id) {
            case R.id.nav_biblio:
                defineNameToolBar("Bibliothèque");
                if (MainActivity.co) {
                    Bundle arg = setArgs(shelfType.MAINSHELF);
                    Shelf shelfFrag = new Shelf();
                    shelfFrag.setArguments(arg);
                    fragmentTransaction.replace(R.id.fragment_container, shelfFrag);
                    fragmentTransaction.commit();
                } else {
                    accessDenied();
                }
                break;
            case R.id.nav_search:
                defineNameToolBar("Recherche");
                Search searchFrag = new Search();
                fragmentTransaction.replace(R.id.fragment_container, searchFrag);
                fragmentTransaction.commit();
                break;
            case R.id.nav_propo:
                defineNameToolBar("Propositions");
                if (MainActivity.co) {
                    Bundle arg = setArgs(shelfType.PROPOSHELF);
                    Shelf shelfFrag = new Shelf();
                    shelfFrag.setArguments(arg);
                    fragmentTransaction.replace(R.id.fragment_container, shelfFrag);
                    fragmentTransaction.commit();
                } else {
                    accessDenied();
                }
                break;
            case R.id.nav_wish:
                defineNameToolBar("Liste de souhaits");
                if (MainActivity.co) {
                    Bundle arg = setArgs(shelfType.WISHSHELF);
                    Shelf shelfFrag = new Shelf();
                    shelfFrag.setArguments(arg);
                    fragmentTransaction.replace(R.id.fragment_container, shelfFrag);
                    fragmentTransaction.commit();
                } else {
                    accessDenied();
                }
                break;
            case R.id.nav_author:
                defineNameToolBar("Auteurs suivis");
                if (MainActivity.co) {
                    FollowAuthor authorFrag = new FollowAuthor();
                    fragmentTransaction.replace(R.id.fragment_container, authorFrag);
                    fragmentTransaction.commit();
                } else {
                    accessDenied();
                }
                break;
            case R.id.nav_co:
                if (!MainActivity.co) {
                    MenuItemCo = item;
                    defineNameToolBar("Connexion");
                    SignIn signFrag = new SignIn();
                    fragmentTransaction.replace(R.id.fragment_container, signFrag);
                    fragmentTransaction.commit();
                } else {
                    MainActivity.co = false;
                    item.setTitle("Connexion");
                    accessDenied();
                }
                break;
            case R.id.nav_param:
                break;
            case R.id.nav_about:
                navAbout();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Bundle setArgs(shelfType st)
    {
        Bundle args = new Bundle();
        args.putSerializable("type", st);

        return args;
    }

    private void accessDenied()
    {
        Disconnected decoFrag = new Disconnected();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, decoFrag);
        fragmentTransaction.commit();
    }

//    private void callShelf(Shelf.shelfType type)
//    {
//        if (_shelf == null) {
//            _shelf = new Shelf(this, type, _scan);
//        } else {
//            _shelf.dataUpdated();
//            switch (type)
//            {
//                case MAINSHELF:
//                    _shelf.mainShelf(_scan);
//                    break;
//                case PROPOSHELF:
//                    _shelf.propoShelf();
//                    break;
//                case WISHSHELF:
//                    _shelf.whishShelf();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    private void setAdapters()
//    {
//        _adapterAutor = new customAdapterAuthor(this, _modelListAutor);
//        _lvAutor.setAdapter(_adapterAutor);
//        _lvAutor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                Snackbar snackbar = Snackbar.make(_lmain, ((TextView) view.findViewById(R.id.TVAutor)).getText(), Snackbar.LENGTH_LONG);
//                snackbar.show();
//            }
//        });
//
//        _adapterCom = new customAdapterCom(this, _modelListCom);
//        _lvCom.setAdapter(_adapterCom);
//
//        _modelListAutor.add("J. K. Rowling");
//        _modelListAutor.add("R. R. Martin");
//        _modelListCom.add(new ComAdapter("Maxime", "23/02/2016 à 13h42", "Super livre !"));
//
//        _lvAutor.setVisibility(View.GONE);
//
//        getTotalHeightofListView();
//    }
//
//    public void getTotalHeightofListView()
//    {
//
//        ListAdapter mAdapter = _lvCom.getAdapter();
//
//        int totalHeight = 0;
//
//        for (int i = 0; i < mAdapter.getCount(); i++)
//        {
//            View mView = mAdapter.getView(i, null, _lvCom);
//
//            mView.measure( View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            totalHeight += mView.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = _lvCom.getLayoutParams();
//        params.height = totalHeight + (_lvCom.getDividerHeight() * (mAdapter.getCount() - 1));
//        _lvCom.setLayoutParams(params);
//        _lvCom.requestLayout();
//
//    }
//
    private void defineNameToolBar(String title)
    {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }
//
//    protected void changeCurrentView(int idFlipper, int idView, boolean clear)
//    {
//        if (clear && _shelf != null) {
//            _shelf.dataUpdated();
//        }
//        ViewFlipper vs = (ViewFlipper) findViewById(idFlipper);
//        vs.setDisplayedChild(vs.indexOfChild(findViewById(idView)));
//        Log.i("App", Integer.toString(idView));
//        _previousViewID = _currentViewID;
//        _currentViewID = idView;
//    }
//
//    public void sendCom(View v)
//    {
//        EditText et = (EditText) findViewById(R.id.ETCom);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH'h'mm", Locale.FRANCE);
//        String currentDateandTime = sdf.format(new Date());
//        Log.i("sendCom", currentDateandTime);
//        _modelListCom.add(new ComAdapter("Nicolas", currentDateandTime, et.getText().toString()));
//        et.setText("");
//        InputMethodManager inputManager =
//                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (this.getCurrentFocus() != null) {
//            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//        getTotalHeightofListView();
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent intent)
//    {
//        if (requestCode == 0x0000c0de) {
//            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//            if (resultCode == RESULT_OK) {
//                if (scanningResult != null) {
//                    String scanContent = scanningResult.getContents();
//                    String scanFormat = scanningResult.getFormatName();
//                    //we have a result
//                    Log.i("App", "Format: " + scanFormat + "\nRésultat: " + scanContent);
//                    changeCurrentView(R.id.VFMain, R.id.SVBook, false);
//                    moreDataBook("La damnation de Pythos", null);
//                    // Ici appeler la fonction moredatabook avec le titre du livre scanné
//                }
//                else {
//                    Log.i("App", "Scan échec");
//                }
//            } else if (resultCode == RESULT_CANCELED) {
//                // Handle cancel
//                Log.i("App", "Scan unsuccessful");
//            }
//        }
//    }
//
//    protected void moreDataBook(String title, List<Book> infos)
//    {
//        int i;
//
//        if (infos == null) {
//            infos = tempHardTest();
//        }
//        if (infos != null) {
//            for (i = 0; i < infos.size(); i++) {
//                if (title.equals(infos.get(i).getTitre())) {
//                    Book b = infos.get(i);
//                    TextView tv = (TextView) findViewById(R.id.TVInfoBook);
//                    TextView tvt = (TextView) findViewById(R.id.TVTitreBook);
//                    TextView tvr = (TextView) findViewById(R.id.TVResum);
//                    tvt.setText(b.getTitre());
//                    tv.setText("Date de sortie : " + b.getDate());
//                    tv.setText(tv.getText() + "\nAuteur : " + b.getAuteur());
//                    tv.setText(tv.getText() + "\nGenre : " + b.getGenre());
//                    tv.setText(tv.getText() + "\nNote : " + b.getNote().toString());
//                    tvr.setText(b.getResum());
//
//                    /*ImageView iv = (ImageView) findViewById(R.id.IVBook);
//                    iv.setImageBitmap(OptimizeBitmap.decodeSampledBitmapFromResource(getResources(), b.getImage(), 200, 200));*/
//                }
//            }
//        }
//    }
//
    private void navAbout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Changelog:\n-V0.00.1: Version alpha\n\n" +
                "L'application BookShelf vous permet de gérer votre bibliothèque !\n" +
                "Développée par Maxime, Geoffrey, Nicolas, Pierre, Victorien et Sergen.\n" +
                "D'après l'idée originale de Pierre.");
        builder.setPositiveButton("Merci !", null);
        builder.show();
    }
//
//    private void navCo()
//    {
//        if (_co == false) {
//            changeCurrentView(R.id.VFMain, R.id.RLSign, false);
//            defineNameToolBar("Connexion");
//        } else {
//            Snackbar snackbar = Snackbar.make(_lmain, "À bientôt !", Snackbar.LENGTH_LONG);
//            snackbar.show();
//            _itm.setTitle("Connexion");
//            _co = false;
//            _gvBiblio.setVisibility(View.GONE);
//            _lvAutor.setVisibility(View.GONE);
//            findViewById(R.id.TVCo).setVisibility(View.VISIBLE);
//        }
//    }
//
    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
//
//    private void verifyUser(int id)
//    {
//        TextView tv = (TextView) findViewById(R.id.TVCo);
//
//        if ((id == R.id.nav_biblio || id == R.id.nav_propo || id == R.id.nav_wish) && _co &&
//                (_gvBiblio.getVisibility() == View.GONE || _lvAutor.getVisibility() == View.GONE)) {
//            _gvBiblio.setVisibility(View.VISIBLE);
//            _lvAutor.setVisibility(View.VISIBLE);
//            tv.setVisibility(View.GONE);
//        } else if (!_co &&
//                (_gvBiblio.getVisibility() == View.VISIBLE || _lvAutor.getVisibility() == View.VISIBLE)) {
//            _gvBiblio.setVisibility(View.GONE);
//            _lvAutor.setVisibility(View.GONE);
//            tv.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private List<Book> tempHardTest()
//    {
//        List<Book> infos = null;
//        /**
//         * ouverture + creation du fichier
//         */
//        SharedPreferences sp = getSharedPreferences("Bibliothèque", Context.MODE_PRIVATE);
//        /**
//         *  edition du fichier
//         */
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("biblio", "[\n" +
//                "{\"titre\":\"Le trône de fer tome 5\",\n" +
//                "\"image\":2130837592,\n" +
//                "\"auteur\":\"Georges R. R. Martin\",\n" +
//                "\"date\":\"08/04/2015\",\n" +
//                "\"genre\":\"fantastique\",\n" +
//                "\"resum\":\"Daenerys règne sur une cité de mort, entourée d'ennemis, tandis que Jon Snow doit affronter ses adversaires des deux côtés du Mur.\",\n" +
//                "\"note\":4,\n" +
//                "\"isbn\":9782290107096},\n" +
//                "{\"titre\":\"Le trône de fer tome 1\",\n" +
//                "\"image\":2130837573,\n" +
//                "\"auteur\":\"Georges R. R. Martin\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\"titre\":\"Le trône de fer tome 2\",\n" +
//                "\"image\":2130837574,\n" +
//                "\"auteur\":\"Georges R. R. Martin\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\"titre\":\"Le trône de fer tome 3\",\n" +
//                "\"image\":2130837575,\n" +
//                "\"auteur\":\"Georges R. R. Martin\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\"titre\":\"Le trône de fer tome 4\",\n" +
//                "\"image\":2130837576,\n" +
//                "\"auteur\":\"Georges R. R. Martin\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\"titre\":\"Harry Potter à l'école des sorciers\",\n" +
//                "\"image\":2130837577,\n" +
//                "\"auteur\":\"J. K. Rowling\",\n" +
//                "\"date\":\"16 novembre 1998\",\n" +
//                "\"genre\":\"Fantastique\",\n" +
//                "\"resum\":\"Le jour de ses onze ans, Harry Potter, un orphelin élevé par un oncle et une tante qui le détestent, voit son existence bouleversée. Un géant vient le chercher pour l'emmener à Poudlard, une école de sorcellerie ! Voler en balai, jeter des sorts, combattre les trolls : Harry Potter se révèle un sorcier doué. Mais un mystère entoure sa naissance et l'effroyable V..., le mage dont personne n'ose prononcer le nom.\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":2070518426\n" +
//                "},\n" +
//                "{\"titre\":\"Harry Potter et la Chambre des secrets\",\n" +
//                "\"image\":2130837578,\n" +
//                "\"auteur\":\"J. K. Rowling\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"Fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\"titre\":\"Harry Potter et le Prisonnier d'Azkaban\",\n" +
//                "\"image\":2130837579,\n" +
//                "\"auteur\":\"J. K. Rowling\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"Fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\"titre\":\"Harry Potter et la Coupe de feu\",\n" +
//                "\"image\":2130837580,\n" +
//                "\"auteur\":\"J. K. Rowling\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"Fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\"titre\":\"Harry Potter et l'Ordre du phénix\",\n" +
//                "\"image\":2130837581,\n" +
//                "\"auteur\":\"J. K. Rowling\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"Fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\"titre\":\"Harry Potter et le Prince de sang-mêlé\",\n" +
//                "\"image\":2130837582,\n" +
//                "\"auteur\":\"J. K. Rowling\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"Fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\"titre\":\"Harry Potter et les reliques de la mort\",\n" +
//                "\"image\":2130837593,\n" +
//                "\"auteur\":\"J. K. Rowling\",\n" +
//                "\"date\":\"\",\n" +
//                "\"genre\":\"Fantastique\",\n" +
//                "\"resum\":\"\",\n" +
//                "\"note\":3,\n" +
//                "\"isbn\":0\n" +
//                "},\n" +
//                "{\n" +
//                "\"titre\":\"La damnation de Pythos\",\n" +
//                "\"image\":2130837595,\n" +
//                "\"auteur\":\"David Annandale\",\n" +
//                "\"date\":\"16 novembre 2015\",\n" +
//                "\"genre\":\"Science-Fiction\",\n" +
//                "\"resum\":\"A la suite du Massacre du Dropsite d'Isstvan V, une force battue et ensanglantée d'Iron Hands, de Raven Guard et de Salamanders se regroupe sur ce qui ressemble à un monde mort insignifiant. En parant des attaques de toutes part commises par les créatures monstrueuses, ces alliés hargneux trouvent un espoir en la forme de réfugiés humains fuyant cette guerre grandissante et jetés à la dérive dans les courants du warp. Mais alors même que les Space Marines se taillent un sanctuaire pour se réfugier dans les jungles de Pythos, une ombre grandissante se rassemble et se prépare à les consumer tous.\",\n" +
//                "\"note\":5,\n" +
//                "\"isbn\":9781780302416\n" +
//                "},\n" +
//                "{\n" +
//                "\"titre\":\"Percy Jackson - Tome 1 : Le voleur de foudre\",\n" +
//                "\"image\":2130837594,\n" +
//                "\"auteur\":\" Rick Riordan\",\n" +
//                "\"date\":\" 3 juillet 2013\",\n" +
//                "\"genre\":\"Fantastique\",\n" +
//                "\"resum\":\"Percy Jackson n’est pas un garçon comme les autres. Ado perturbé, renvoyé de collège en pension, il découvre un jour le secret de sa naissance et de sa différence : son père, qu’il n’a jamais connu, n’est autre que Poséidon, le dieu de la mer dans la mythologie grecque. Placé pour sa protection dans un camp de vacances pour enfants « sangs mêlés » (mi-humains, mi-divins), Percy se voit injustement accusé d’avoir volé l’éclair de Zeus. Afin d’éviter une guerre fratricide entre les dieux de l’Olympe, il va devoir repartir dans le monde des humains, retrouver l’éclair et démasquer le vrai coupable… au péril de sa vie.\",\n" +
//                "\"note\":4.5,\n" +
//                "\"isbn\":2226249303\n" +
//                "}\n" +
//                "]");
//        editor.apply();
//        /**
//         * récupération des donnée enregistré
//         */
//        String ret = sp.getString("biblio", null);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            infos = objectMapper.readValue(ret, objectMapper.getTypeFactory().constructCollectionType(List.class, Book.class));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return infos;
//    }
}