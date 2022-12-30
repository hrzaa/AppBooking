package com.example.appbooking.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.appbooking.R;
import com.example.appbooking.database.DatabaseHelper;

import java.util.Calendar;

public class BookWisataActivity extends AppCompatActivity {

    protected Cursor cursor;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinWisata, spinHotel, spinDewasa, spinAnak;
//    SessionManager session;
    String email;
    int id_book;
    public String sWisata, sHotel, sTanggal, sDewasa, sAnak;
    int jmlDewasa, jmlAnak;
    int hargaDewasa, hargaAnak;
    int hargaTotalDewasa, hargaTotalAnak, hargaTotal;
    private EditText etTanggal;
    private DatePickerDialog dpTanggal;
    Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_wisata);

        dbHelper = new DatabaseHelper(BookWisataActivity.this);
        db = dbHelper.getReadableDatabase();

        final String[] wisata = {"Keraton", "Cetho", "Sukuh", "SakuraHils", "Madirda"};
        final String[] hotel = {"Sakura", "Mawar", "Melati", "Kamboja"};
        final String[] dewasa = {"0", "1", "2", "3", "4", "5"};
        final String[] anak = {"0", "1", "2", "3", "4", "5"};

        spinWisata = findViewById(R.id.wisata);
        spinHotel = findViewById(R.id.hotel);
        spinDewasa = findViewById(R.id.dewasa);
        spinAnak = findViewById(R.id.anak);

        ArrayAdapter<CharSequence> adapterWisata = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, wisata);
        adapterWisata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinWisata.setAdapter(adapterWisata);

        ArrayAdapter<CharSequence> adapterHotel = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, hotel);
        adapterHotel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinHotel.setAdapter(adapterHotel);

        ArrayAdapter<CharSequence> adapterDewasa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, dewasa);
        adapterDewasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDewasa.setAdapter(adapterDewasa);

        ArrayAdapter<CharSequence> adapterAnak = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, anak);
        adapterAnak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAnak.setAdapter(adapterAnak);

        spinWisata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sWisata = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinHotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sHotel = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDewasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sDewasa = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinAnak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAnak = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnBook = findViewById(R.id.book);

        etTanggal = findViewById(R.id.tanggal_pemesanan);
        etTanggal.setInputType(InputType.TYPE_NULL);
        etTanggal.requestFocus();
//        session = new SessionManager(getApplicationContext());
//        HashMap<String, String> user = session.getUserDetails();
//        email = user.get(SessionManager.KEY_EMAIL);
        setDateTimeField();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perhitunganHarga();
                if (sWisata != null && sHotel != null && sTanggal != null && sDewasa != null) {
                    if ((sWisata.equalsIgnoreCase("jakarta") && sHotel.equalsIgnoreCase("jakarta"))
                            || (sWisata.equalsIgnoreCase("bandung") && sHotel.equalsIgnoreCase("bandung"))
                            || (sWisata.equalsIgnoreCase("purwokerto") && sHotel.equalsIgnoreCase("purwokerto"))
                            || (sWisata.equalsIgnoreCase("yogyakarta") && sHotel.equalsIgnoreCase("yogyakarta"))
                            || (sWisata.equalsIgnoreCase("surabaya") && sHotel.equalsIgnoreCase("surabaya"))) {
                        Toast.makeText(BookWisataActivity.this, "Asal dan Tujuan tidak boleh sama !", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(BookWisataActivity.this)
                                .setTitle("Ingin pesan paket wisata sekarang?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            db.execSQL("INSERT INTO TB_BOOK (asal, tujuan, tanggal, dewasa, anak) VALUES ('" +
                                                    sWisata + "','" +
                                                    sHotel + "','" +
                                                    sTanggal + "','" +
                                                    sDewasa + "','" +
                                                    sAnak + "');");
                                            cursor = db.rawQuery("SELECT id_book FROM TB_BOOK ORDER BY id_book DESC", null);
                                            cursor.moveToLast();
                                            if (cursor.getCount() > 0) {
                                                cursor.moveToPosition(0);
                                                id_book = cursor.getInt(0);
                                            }
                                            db.execSQL("INSERT INTO TB_HARGA (username, id_book, harga_dewasa, harga_anak, harga_total) VALUES ('" +
                                                    email + "','" +
                                                    id_book + "','" +
                                                    hargaTotalDewasa + "','" +
                                                    hargaTotalAnak + "','" +
                                                    hargaTotal + "');");
                                            Toast.makeText(BookWisataActivity.this, "Booking berhasil", Toast.LENGTH_LONG).show();
                                            finish();
                                        } catch (Exception e) {
                                            Toast.makeText(BookWisataActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(BookWisataActivity.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbWisata);
        toolbar.setTitle("Form Booking");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void perhitunganHarga() {
        if (sWisata.equalsIgnoreCase("keraton") && sHotel.equalsIgnoreCase("sakura")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sWisata.equalsIgnoreCase("keraton") && sHotel.equalsIgnoreCase("mawar")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sWisata.equalsIgnoreCase("keraton") && sHotel.equalsIgnoreCase("melati")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sWisata.equalsIgnoreCase("keraton") && sHotel.equalsIgnoreCase("kamboja")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sWisata.equalsIgnoreCase("cetho") && sHotel.equalsIgnoreCase("sakura")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sWisata.equalsIgnoreCase("cetho") && sHotel.equalsIgnoreCase("mawar")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sWisata.equalsIgnoreCase("cetho") && sHotel.equalsIgnoreCase("melati")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sWisata.equalsIgnoreCase("cetho") && sHotel.equalsIgnoreCase("kamboja")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sWisata.equalsIgnoreCase("sukuh") && sHotel.equalsIgnoreCase("sakura")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sWisata.equalsIgnoreCase("sukuh") && sHotel.equalsIgnoreCase("mawar")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sWisata.equalsIgnoreCase("sukuh") && sHotel.equalsIgnoreCase("melati")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sWisata.equalsIgnoreCase("sukuh") && sHotel.equalsIgnoreCase("kamboja")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;
        } else if (sWisata.equalsIgnoreCase("sakurahils") && sHotel.equalsIgnoreCase("sakura")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sWisata.equalsIgnoreCase("sakurahils") && sHotel.equalsIgnoreCase("mawar")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sWisata.equalsIgnoreCase("sakurahils") && sHotel.equalsIgnoreCase("melati")) {
            hargaDewasa = 80000;
            hargaAnak = 40000;
        } else if (sWisata.equalsIgnoreCase("sakurahils") && sHotel.equalsIgnoreCase("kamboja")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sWisata.equalsIgnoreCase("madirda") && sHotel.equalsIgnoreCase("sakura")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sWisata.equalsIgnoreCase("madirda") && sHotel.equalsIgnoreCase("mawar")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sWisata.equalsIgnoreCase("madirda") && sHotel.equalsIgnoreCase("melati")) {
            hargaDewasa = 80000;
            hargaAnak = 40000;
        } else if (sWisata.equalsIgnoreCase("madirda") && sHotel.equalsIgnoreCase("kamboja")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;
        }

        jmlDewasa = Integer.parseInt(sDewasa);
        jmlAnak = Integer.parseInt(sAnak);

        hargaTotalDewasa = jmlDewasa * hargaDewasa;
        hargaTotalAnak = jmlAnak * hargaAnak;
        hargaTotal = hargaTotalDewasa + hargaTotalAnak;
    }

    private void setDateTimeField() {
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpTanggal.show();
            }
        });

        dpTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggal = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                etTanggal.setText(sTanggal);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}