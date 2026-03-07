package com.example.contactsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditContactActivity extends AppCompatActivity {

    // عناصر الواجهة
    private EditText edtName, edtPhone, edtCompany, edtNotes;
    private Button btnSave;
    private int position; // موقع العنصر في القائمة

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        // ربط Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarEdit);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // زر الرجوع
            getSupportActionBar().setTitle(getString(R.string.edit)); // عنوان الشاشة "Edit"
        }

        // ربط عناصر الواجهة
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtCompany = findViewById(R.id.edtCompany);
        edtNotes = findViewById(R.id.edtNotes);
        btnSave = findViewById(R.id.btnSave);

        // استلام البيانات من DetailsActivity
        Intent intent = getIntent();
        String name = intent.getStringExtra("EXTRA_NAME");
        String phone = intent.getStringExtra("EXTRA_MOBILE");
        String company = intent.getStringExtra("EXTRA_COMPANY");
        String notes = intent.getStringExtra("EXTRA_NOTES");
        position = intent.getIntExtra("EXTRA_POSITION", -1);

        // عرض البيانات القديمة في حقول الإدخال
        edtName.setText(name);
        edtPhone.setText(phone);
        edtCompany.setText(company);
        edtNotes.setText(notes);

        // زر الحفظ
        btnSave.setOnClickListener(v -> {
            // قراءة القيم الجديدة من الحقول
            String newName = edtName.getText().toString().trim();
            String newPhone = edtPhone.getText().toString().trim();
            String newCompany = edtCompany.getText().toString().trim();
            String newNotes = edtNotes.getText().toString().trim();

            // التحقق من أن الاسم والرقم غير فارغين
            if (newName.isEmpty() || newPhone.isEmpty()) {
                Toast.makeText(EditContactActivity.this, "الرجاء إدخال الاسم ورقم الجوال", Toast.LENGTH_SHORT).show();
            } else {
                // تجهيز البيانات الجديدة للعودة إلى DetailsActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("EXTRA_NAME", newName);
                resultIntent.putExtra("EXTRA_MOBILE", newPhone);
                resultIntent.putExtra("EXTRA_COMPANY", newCompany);
                resultIntent.putExtra("EXTRA_NOTES", newNotes);
                resultIntent.putExtra("EXTRA_POSITION", position);

                // إعادة النتيجة وإغلاق الشاشة
                setResult(RESULT_OK, resultIntent);
                finish(); // العودة مباشرة للـ DetailsActivity
            }
        });
    }

    // التعامل مع زر الرجوع في الـ Toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // إغلاق الشاشة والعودة
        return true;
    }
}
