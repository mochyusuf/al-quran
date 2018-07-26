package badru.tajwid.leftcornerparsing;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog ConnectDialog;
    private CustomAdapter adapter;
    private ListView ListKamus;
    private NavigationView navigationView;
    private ArrayList<HashMap<String, Object>> arrayAsli,arrayHasil;
    private static String URL_SERVER;
    private Menu menuCari;
    private ImageView imgLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        URL_SERVER=getResources().getString(R.string.uri_server);
        setContentView(R.layout.splash_screen);
        ConnectDialog = new ProgressDialog(this);
        ConnectDialog.setMessage("Mengambil data...");
        ConnectDialog.setCancelable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);toggle.syncState();

                navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(MainActivity.this);
                imgLogo = (ImageView) findViewById(R.id.imgLogo);
                imgLogo.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.zoomin));
                imgLogo.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.zoomout));
            }
            private void finish() {
            }
        }, 3000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuCari=menu;
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String searchQuery) {
                adapter.Algoritma_BFS(searchQuery.toString().trim());
                ListKamus.invalidate();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_materi) {
            /*((RelativeLayout) findViewById(R.id.layout_utama)).removeAllViewsInLayout();
            getSupportActionBar().setTitle("Materi Tajwid");
            ((RelativeLayout) findViewById(R.id.layout_utama)).addView(getLayoutInflater().inflate(R.layout.materi_tajwid, (RelativeLayout)findViewById(R.id.layout_utama),false));
            sendRequest();*/
            startActivity(new Intent(MainActivity.this,MateriTajwid.class));
        } else if (id == R.id.nav_ketik) {
            /*menuCari.findItem(R.id.action_search).setVisible(false);
            ((RelativeLayout) findViewById(R.id.layout_utama)).removeAllViewsInLayout();
            getSupportActionBar().setTitle("Tentang Pembuat");
            ((RelativeLayout) findViewById(R.id.layout_utama)).addView(getLayoutInflater().inflate(R.layout.about_me, (RelativeLayout)findViewById(R.id.layout_utama),false));*/
            startActivity(new Intent(MainActivity.this,PeriksaTajwid.class));
        } else if (id == R.id.nav_pilihan) {
            //startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            /*new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Konfimasi keluar")
                    .setMessage("Apakah yakin akan keluar?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Tidak",null).show();*/
            startActivity(new Intent(MainActivity.this,SuratPilihan.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class CustomAdapter extends ArrayAdapter<HashMap<String, Object>>{
        public CustomAdapter(Context context, int resource, ArrayList<HashMap<String, Object>> strings) {
            super(context, resource, strings);
        }

        public class ViewInit {
            TextView istilah;
        }

        ViewInit VI;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_listkamus,null);
                VI = new ViewInit();
                VI.istilah = (TextView) convertView.findViewById(R.id.txtistilah);
                convertView.setTag(VI);
            } else {
                VI = (ViewInit) convertView.getTag();
            }
            VI.istilah.setText(arrayHasil.get(position).get("istilah").toString());
            return convertView;
        }

        public void Algoritma_BFS(String charText) {
            HashMap<String,Object> hasil;
            charText = charText.toLowerCase(Locale.getDefault());
            hasil=new HashMap<String,Object>();
            arrayHasil.clear();
            if (charText.length() == 0) {
                arrayHasil.addAll(arrayAsli);
            } else {
                for (int i = 0; i < arrayAsli.size(); i++) {
                    String source = arrayAsli.get(i).get("istilah").toString();
                    if (charText.length() != 0 && source.toLowerCase(Locale.getDefault()).startsWith(charText)) {
                        arrayHasil.add(arrayAsli.get(i));
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}