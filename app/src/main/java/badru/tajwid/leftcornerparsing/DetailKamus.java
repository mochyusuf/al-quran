package badru.tajwid.leftcornerparsing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

public class DetailKamus extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_listkamus);
        HashMap<String,Object> data= (HashMap<String, Object>) getIntent().getExtras().get("data");
        TextView txtIstilah=(TextView) findViewById(R.id.txtIstilah);
        setTitle(data.get("istilah").toString());
        txtIstilah.setText(data.get("arti").toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
