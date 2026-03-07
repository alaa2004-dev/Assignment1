package com.example.contactsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private ArrayList<Contact> contacts;
    private boolean isLinearLayout = true; // نوع العرض الحالي
    private SharedPreferences prefs;       // لتخزين نوع العرض
    private TextView txtNoResults;         // نص يظهر إذا لم توجد نتائج بحث

    private static final int REQUEST_ADD = 100;
    private static final int REQUEST_DETAILS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  إعداد الـ Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);

        //  إعداد RecyclerView
        recyclerView = findViewById(R.id.recyclerViewContacts);
        txtNoResults = findViewById(R.id.txtNoResults);

        // استرجاع نوع العرض من SharedPreferences
        prefs = getSharedPreferences("LayoutPrefs", MODE_PRIVATE);
        isLinearLayout = prefs.getBoolean("isLinearLayout", true);

        //  ضبط نوع العرض (قائمة أو شبكة)
        if (isLinearLayout) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        //  إنشاء قائمة جهات الاتصال الافتراضية
        contacts = new ArrayList<>();
        String[] names = {"Omar", "Ali", "Sara", "Lina", "Hani", "Mona", "Khaled", "Nour"};
        String[] phones = {"0568794568", "0593333333", "0592222222", "0594444444",
                "0595555555", "0596666666", "0597777777", "0598888888"};

        for (int i = 0; i < names.length; i++) {
            contacts.add(new Contact(names[i], phones[i]));
        }

        adapter = new ContactAdapter(contacts, txtNoResults);
        recyclerView.setAdapter(adapter);

        //  إعداد البحث
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setBackgroundResource(R.drawable.search_background);
        searchView.setQueryHint("Search by name...");
        TextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setHintTextColor(Color.parseColor("#B0B0B0"));
        searchText.setTextColor(Color.BLACK);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        //  زر إضافة جهة اتصال جديدة
        FloatingActionButton fabAddContact = findViewById(R.id.fabAddContact);
        fabAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivityForResult(intent, REQUEST_ADD);
        });

        //  السحب لحذف عنصر
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) { return false; }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                contacts.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    //  استقبال النتائج من الأنشطة الأخرى (إضافة أو تعديل)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // إضافة جهة اتصال جديدة
        if (requestCode == REQUEST_ADD && resultCode == RESULT_OK) {
            String name = data.getStringExtra("EXTRA_NAME");
            String phone = data.getStringExtra("EXTRA_MOBILE");
            String company = data.getStringExtra("EXTRA_COMPANY");
            String notes = data.getStringExtra("EXTRA_NOTES");

            contacts.add(new Contact(name, phone, company, notes));
            adapter.notifyItemInserted(contacts.size() - 1);
            recyclerView.scrollToPosition(contacts.size() - 1);

            Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();
        }

        // تعديل جهة اتصال موجودة
        if (requestCode == REQUEST_DETAILS && resultCode == RESULT_OK) {
            int position = data.getIntExtra("EXTRA_POSITION", -1);
            if (position != -1) {
                String newName = data.getStringExtra("EXTRA_NAME");
                String newPhone = data.getStringExtra("EXTRA_MOBILE");
                String newCompany = data.getStringExtra("EXTRA_COMPANY");
                String newNotes = data.getStringExtra("EXTRA_NOTES");

                Contact updated = contacts.get(position);
                updated.setName(newName);
                updated.setPhone(newPhone);
                updated.setCompany(newCompany);
                updated.setNotes(newNotes);

                adapter.notifyItemChanged(position);
                Toast.makeText(this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  قائمة الخيارات
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_switch_layout) {
            if (isLinearLayout) {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                isLinearLayout = false;
                Toast.makeText(this, "Switched to Grid layout", Toast.LENGTH_SHORT).show();
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                isLinearLayout = true;
                Toast.makeText(this, "Switched to Linear layout", Toast.LENGTH_SHORT).show();
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLinearLayout", isLinearLayout);
            editor.apply();
            return true;
        }

        //  حذف جميع جهات الاتصال
        else if (id == R.id.action_delete_all) {
            contacts.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "All contacts deleted", Toast.LENGTH_SHORT).show();
            return true;
        }

        //  نافذة "حول التطبيق"
        else if (id == R.id.action_about) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("About ContactsApp")
                    .setMessage("ContactsApp v1.0\n\nDeveloped by Eng Alaa Barak\n© 2026\n\nThis app helps you manage contacts with search, add, edit, delete, and share features.")
                    .setPositiveButton("Close", null)
                    .create();
            dialog.show();
            return true;
        }

        //  مشاركة أول جهة اتصال
        else if (id == R.id.action_share) {
            if (!contacts.isEmpty()) {
                Contact contact = contacts.get(0);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Name: " + contact.getName() + "\nPhone: " + contact.getPhone());
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            } else {
                Toast.makeText(this, "No contacts to share", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        // إنهاء التطبيق
        else if (id == R.id.action_exit) {
            finishAffinity(); // إغلاق جميع الأنشطة
            System.exit(0);   // إنهاء التطبيق
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
