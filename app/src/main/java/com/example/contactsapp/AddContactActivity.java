package com.example.contactsapp;

// استيراد المكتبات اللازمة
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.imageview.ShapeableImageView;

public class AddContactActivity extends AppCompatActivity {

    // ثابت لتحديد كود الطلب عند اختيار صورة من المعرض
    private static final int PICK_IMAGE_REQUEST = 1;

    // عناصر الواجهة
    private EditText editTextFirstName, editTextLastName, editTextCompany, editTextPhone, editTextNotes;
    private Button buttonSave;
    private ShapeableImageView imageProfile; // صورة البروفايل
    private Uri selectedImageUri; // لتخزين مسار الصورة المختارة

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // ربط الـ Toolbar الرسمي
        Toolbar toolbar = findViewById(R.id.toolbarAdd);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // زر الرجوع
            getSupportActionBar().setTitle(getString(R.string.add_contact)); // العنوان "Add Contact"
        }

        // ربط عناصر الواجهة
        imageProfile = findViewById(R.id.imageProfile);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextCompany = findViewById(R.id.editTextCompany);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextNotes = findViewById(R.id.editTextNotes);
        buttonSave = findViewById(R.id.buttonSave);

        // عند الضغط على صورة البروفايل  افتح المعرض لاختيار صورة
        imageProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*"); // السماح باختيار صور فقط
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // زر الحفظ
        buttonSave.setOnClickListener(v -> {
            // قراءة النصوص من حقول الإدخال
            String firstName = editTextFirstName.getText().toString().trim();
            String lastName = editTextLastName.getText().toString().trim();
            String company = editTextCompany.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();
            String notes = editTextNotes.getText().toString().trim();

            // دمج الاسم الأول مع الأخير
            String fullName = firstName + " " + lastName;

            // التحقق من أن الاسم والرقم غير فارغين
            if (fullName.trim().isEmpty() || phone.isEmpty()) {
                Toast.makeText(AddContactActivity.this, "الرجاء إدخال الاسم ورقم الجوال", Toast.LENGTH_SHORT).show();
            } else {
                // تجهيز البيانات للعودة إلى MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("EXTRA_NAME", fullName);
                resultIntent.putExtra("EXTRA_MOBILE", phone);
                resultIntent.putExtra("EXTRA_COMPANY", company);
                resultIntent.putExtra("EXTRA_NOTES", notes);

                // إذا تم اختيار صورة، أرسلها أيضاً
                if (selectedImageUri != null) {
                    resultIntent.putExtra("EXTRA_IMAGE_URI", selectedImageUri.toString());
                }

                // إعادة النتيجة وإغلاق الشاشة
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    // التعامل مع زر الرجوع في الـ Toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // إغلاق الشاشة والعودة
        return true;
    }

    // استقبال الصورة المختارة من المعرض
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // إذا المستخدم اختار صورة بنجاح
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // حفظ مسار الصورة
            imageProfile.setImageURI(selectedImageUri); // عرض الصورة المختارة في الواجهة
        }
    }
}
