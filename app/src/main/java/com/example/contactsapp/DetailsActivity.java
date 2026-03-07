package com.example.contactsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailsActivity extends AppCompatActivity {

    // عناصر الواجهة
    private TextView txtDetailName, txtDetailMobile, txtProfileLetter;
    private ImageButton btnCall, btnMessage, btnEmail, btnVideo;
    private TextView txtNormalCallNumber, txtWhatsAppNumber, txtEmailLabel;

    // بيانات العنصر
    private int position;
    private String name, mobile, company, notes;

    // لانشر خاص بفتح شاشة التعديل واستقبال النتيجة
    private ActivityResultLauncher<Intent> editContactLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // إعداد الـ Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // زر الرجوع
            getSupportActionBar().setTitle(getString(R.string.details_title)); // عنوان الشاشة
        }

        // ربط عناصر الواجهة
        txtDetailName = findViewById(R.id.txtDetailName);
        txtDetailMobile = findViewById(R.id.txtDetailMobile);
        txtProfileLetter = findViewById(R.id.txtProfileLetter); // الدائرة بالحرف الأول
        txtNormalCallNumber = findViewById(R.id.txtNormalCallNumber);
        txtWhatsAppNumber = findViewById(R.id.txtWhatsAppNumber);
        txtEmailLabel = findViewById(R.id.txtVideoCallNumber);

        btnCall = findViewById(R.id.btnCall);
        btnMessage = findViewById(R.id.btnMessage);
        btnEmail = findViewById(R.id.btnEmail);
        btnVideo = findViewById(R.id.btnVideo);

        // استلام البيانات من الـ Adapter
        Intent intent = getIntent();
        name = intent.getStringExtra("EXTRA_NAME");
        mobile = intent.getStringExtra("EXTRA_MOBILE");
        company = intent.getStringExtra("EXTRA_COMPANY");
        notes = intent.getStringExtra("EXTRA_NOTES");
        position = intent.getIntExtra("EXTRA_POSITION", -1);

        // استلام الحرف ولون الخلفية للدائرة
        String letter = intent.getStringExtra("EXTRA_LETTER");
        int colorRes = intent.getIntExtra("EXTRA_COLOR_RES", R.drawable.circle_background);

        Log.d("DetailsActivity", "Mobile received: " + mobile);

        // عرض البيانات
        txtDetailName.setText(name);
        txtDetailMobile.setText(mobile);

        // عرض الدائرة بالحرف والخلفية
        txtProfileLetter.setText(letter);
        txtProfileLetter.setBackgroundResource(colorRes);

        // النصوص أسفل الأزرار
        txtNormalCallNumber.setText(R.string.call);
        txtWhatsAppNumber.setText(R.string.message);
        txtEmailLabel.setText(R.string.email);

        TextView txtVideoLabel = findViewById(R.id.txtVideoLabel);
        txtVideoLabel.setText(R.string.video);

        // أزرار الإجراءات (اتصال، رسالة، إيميل، فيديو)
        btnCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + mobile));
            startActivity(callIntent);
        });

        btnMessage.setOnClickListener(v -> {
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            smsIntent.setData(Uri.parse("smsto:" + mobile));
            startActivity(smsIntent);
        });

        btnEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:example@email.com"));
            startActivity(emailIntent);
        });

        btnVideo.setOnClickListener(v -> {
            Intent videoIntent = new Intent(Intent.ACTION_VIEW);
            videoIntent.setData(Uri.parse("https://wa.me/" + mobile));
            startActivity(videoIntent);
        });

        // لانشر لتعديل جهة الاتصال واستقبال النتيجة
        editContactLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        // تحديث البيانات داخل DetailsActivity
                        name = data.getStringExtra("EXTRA_NAME");
                        mobile = data.getStringExtra("EXTRA_MOBILE");
                        company = data.getStringExtra("EXTRA_COMPANY");
                        notes = data.getStringExtra("EXTRA_NOTES");

                        txtDetailName.setText(name);
                        txtDetailMobile.setText(mobile);

                        // إرسال التحديث مرة أخرى للـ MainActivity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("EXTRA_NAME", name);
                        resultIntent.putExtra("EXTRA_MOBILE", mobile);
                        resultIntent.putExtra("EXTRA_COMPANY", company);
                        resultIntent.putExtra("EXTRA_NOTES", notes);
                        resultIntent.putExtra("EXTRA_POSITION", position);
                        setResult(RESULT_OK, resultIntent);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // نفخ قائمة التفاصيل
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish(); // زر الرجوع
            return true;
        }

        if (id == R.id.action_edit) {
            // فتح شاشة التعديل
            Intent editIntent = new Intent(DetailsActivity.this, EditContactActivity.class);
            editIntent.putExtra("EXTRA_NAME", name);
            editIntent.putExtra("EXTRA_MOBILE", mobile);
            editIntent.putExtra("EXTRA_COMPANY", company);
            editIntent.putExtra("EXTRA_NOTES", notes);
            editIntent.putExtra("EXTRA_POSITION", position);

            editContactLauncher.launch(editIntent);
            return true;
        } else if (id == R.id.action_delete) {
            // حذف جهة الاتصال
            Intent resultIntent = new Intent();
            resultIntent.putExtra("EXTRA_POSITION", position);
            setResult(RESULT_OK, resultIntent);
            finish();
            return true;
        } else if (id == R.id.action_share) {
            // مشاركة بيانات جهة الاتصال
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Name: " + name + "\nPhone: " + mobile +
                            (company.isEmpty() ? "" : "\nCompany: " + company) +
                            (notes.isEmpty() ? "" : "\nNotes: " + notes));
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }

        return false;
    }
}
