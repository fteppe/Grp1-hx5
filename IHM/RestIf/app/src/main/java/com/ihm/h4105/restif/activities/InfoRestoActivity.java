package com.ihm.h4105.restif.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihm.h4105.restif.Ami;
import com.ihm.h4105.restif.AmiAdapter;
import com.ihm.h4105.restif.R;

import java.util.ArrayList;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.view.View.INVISIBLE;

public class InfoRestoActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static String titleRestau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_resto);

        titleRestau = getIntent().getStringExtra("restau_selected");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(titleRestau);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.food);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(InfoRestoActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu_temps,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Snackbar.make(findViewById(R.id.fab), "Vous mangerez à "+titleRestau+". (invitation envoyées)", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return true;
                    }
                });
                popup.show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_resto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                //noinspection SimplifiableIfStatement
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_info_resto, container, false);
                    TextView textViewHoraires = (TextView) rootView.findViewById(R.id.horaires);
                    TextView textViewPaiement = (TextView) rootView.findViewById(R.id.moyens_paiement);
                    TextView textViewDate = (TextView) rootView.findViewById(R.id.date_menu);
                    TextView textViewEntrees = (TextView) rootView.findViewById(R.id.entrees);
                    TextView textViewPlats = (TextView) rootView.findViewById(R.id.plats);
                    TextView textViewAccompagnements = (TextView) rootView.findViewById(R.id.accompagnements);
                    TextView textViewFromages = (TextView) rootView.findViewById(R.id.fromages);
                    TextView textViewDesserts = (TextView) rootView.findViewById(R.id.desserts);
                    textViewDate.setText("12/01/2017");

                    switch (titleRestau) {
                        case "Castor et Pollux (Le Beurk)":
                            textViewHoraires.setText("11h30 - 13h30");
                            textViewPaiement.setText("Izzly SoldeINSA");
                            textViewEntrees.setText("AVOCAT AU THON\n" +
                                    "ASSIETTE DE JAMBON CRU\n" +
                                    "CELERI REMOULADE A MA FACON\n" +
                                    "MOUSSAKA\n" +
                                    "SALADE COMPOSEE");
                            textViewPlats.setText("BROCHETTE DE DINDE ORIENTALE\n" +
                                    "CORDON BLEU DE POULET\n" +
                                    "TILAPIA SAUCE CREME DE POIVONS\n" +
                                    "FONDUE DE POIREAUX AUX POIS CHICHE (végétarien)");
                            textViewAccompagnements.setText("PENNE RIGATE AU BEURRE\n" +
                                    "PETITS POIS CAROTTES\n" +
                                    "SOUFFLE DE COURGE");
                            textViewFromages.setText("BLEU D’AUVERGNE\n" +
                                    "FROMAGE BLANC FAISSELLE\n" +
                                    "YAOURT AUX FRUITS\n" +
                                    "YAOURT BIFIDUS");
                            textViewDesserts.setText("OEUF A LA NEIGE\n" +
                                    "TARTE AU CHOCOLAT\n" +
                                    "VERRINE PANACOTTA ANANAS ET COCO\n" +
                                    "DESSERTS LACTES\n" +
                                    "FRUITS DE SAISON");
                            break;
                        case "Le Prévert":
                            textViewHoraires.setText("11h30 - 13h30");
                            textViewPaiement.setText("Izzly SoldeINSA");
                            textViewEntrees.setText("SALADE DE TABOULE");
                            textViewPlats.setText("SANDWICH PAVE AU POIVRE\n" +
                                    "SANDWICH CLUB RILLETTE DE SAUMON\n" +
                                    "SUEDOIS NICOIS \n" + "TORSADES A LA BOLOGNAISE");
                            textViewAccompagnements.setText("CHIPS");
                            textViewFromages.setText("YAOURT BIFIDUS");
                            textViewDesserts.setText("DESSERTS LACTES\n" +
                                    "FRUITS DE SAISON");
                            break;
                        case "Le Grillon":
                            textViewHoraires.setText("11h45 - 13h30");
                            textViewPaiement.setText("Izzly SoldeINSA");
                            textViewEntrees.setText("SALADE DE TABOULE");
                            textViewPlats.setText("BURGER AU POULET\n" +
                                    "BURGER FISH");
                            textViewAccompagnements.setText("CHOUX ROMANESCO VAPEUR\n" +
                                    "FRITES STEACK HOUSE\n" +
                                    "POIREAUX A LA CREME");
                            textViewDesserts.setText("YAOURT AUX FRUITS");
                            textViewFromages.setText("TARTE A LA NOIX COCO\n" +
                                    "DESSERTS LACTES\n" +
                                    "FRUITS DE SAISON");
                            break;
                        case "L'Olivier":
                            textViewHoraires.setText("11h45 - 13h30");
                            textViewPaiement.setText("Izzly SoldeINSA");
                            textViewEntrees.setText("SALADE DE TABOULE");
                            textViewPlats.setText("PIZZA AUX 3 FROMAGES\n" +
                                    "PIZZA LA CHASSEUR\n" +
                                    "PIZZA LYONNAISE\n" +
                                    "SPAGHETTIS ET BEIGNET DE POISSON\n" +
                                    "SAUCE A LA CREME DE POIVRONS");
                            textViewFromages.setText("YAOURT AUX FRUITS");
                            textViewDesserts.setText("TARTE A LA NOIX COCO\n" +
                                    "DESSERTS LACTES\n" +
                                    "FRUITS DE SAISON");
                            break;
                    }

                    return rootView;

                case 2:
                    rootView = inflater.inflate(R.layout.fragment_liste_amis, container, false);

                    ListView mListView = (ListView) rootView.findViewById(R.id.liste_amis);
                    mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

                    final ArrayList<Ami> amiList = new ArrayList<Ami>();

                    if(titleRestau.equals("Le Grillon"))
                        amiList.add(new Ami("Delamarre","Marie","Pierre et 3 autres", "personne"));
                    amiList.add(new Ami("Heaton","Charles","", "personne2"));
                    amiList.add(new Ami("Lavernh","Rémi","", "personne3"));


                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println("CLICK OK");
                            CheckBox cBox = (CheckBox) view.findViewById(R.id.checked);

                            if(cBox.isChecked())
                            {
                                cBox.setChecked(false);
                                view.setBackgroundColor(Color.TRANSPARENT);
                            }
                            else
                            {
                                cBox.setChecked(true);
                                view.setBackgroundColor(Color.parseColor("#DCF5B93F"));
                            }
                        }
                    });
                    AmiAdapter adapter = new AmiAdapter(getActivity().getApplicationContext(), amiList);
                    mListView.setAdapter(adapter);

                    return rootView;

                default:
                    return null;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Informations";
                case 1:
                    return "Amis";
            }
            return null;
        }
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
}
