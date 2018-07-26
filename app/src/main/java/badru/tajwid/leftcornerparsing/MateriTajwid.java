package badru.tajwid.leftcornerparsing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MateriTajwid extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spin_materi;
    String[] negara = { "Pengertian Tajwid", "Hukum Nun Mati","Hukum Izhar", "Hukum Iqlab",
            "Hukum Idgham", "Hukum Ikhfa"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.materi_tajwid);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, negara);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_materi = (Spinner) findViewById(R.id.spin_materi);
        spin_materi.setAdapter(aa);
        spin_materi.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setTitle("Materi Tajwid \u21A0"+negara[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
