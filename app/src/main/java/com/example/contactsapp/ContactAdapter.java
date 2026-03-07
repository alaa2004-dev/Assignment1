package com.example.contactsapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements Filterable {
    // القائمة الحالية المعروضة في RecyclerView
    private ArrayList<Contact> contacts;
    // نسخة كاملة من القائمة تستخدم للبحث/الفلترة
    private ArrayList<Contact> contactsFull;
    // نص يظهر إذا لم يتم العثور على نتائج في البحث
    private TextView txtNoResults;

    // الكونستركتور: يستقبل القائمة والنص من MainActivity
    public ContactAdapter(ArrayList<Contact> contacts, TextView txtNoResults) {
        this.contacts = contacts;
        contactsFull = new ArrayList<>(contacts); // حفظ نسخة من القائمة الأصلية
        this.txtNoResults = txtNoResults;
    }

    // إنشاء ViewHolder وربط ملف XML الخاص بالعنصر
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    // ربط البيانات مع عناصر الواجهة لكل عنصر
    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        // عرض الاسم
        holder.txtName.setText(contact.getName());

        // عرض أول حرف داخل الدائرة
        String firstLetter = contact.getName().substring(0, 1).toUpperCase();
        holder.imgContact.setText(firstLetter);

        // اختيار لون الخلفية حسب أول حرف
        int bgRes;
        char firstChar = contact.getName().toUpperCase().charAt(0);
        switch (firstChar) {
            case 'A': bgRes = R.drawable.circle_green; break;
            case 'O': bgRes = R.drawable.circle_orange; break;
            case 'S': bgRes = R.drawable.circle_red; break;
            case 'M': bgRes = R.drawable.circle_blue; break;
            case 'K': bgRes = R.drawable.circle_brown; break;
            case 'N': bgRes = R.drawable.circle_skyblue; break;
            case 'H': bgRes = R.drawable.circle_grayblue; break;
            case 'L': bgRes = R.drawable.circle_teal; break;
            case 'E': bgRes = R.drawable.circle_yellow; break;
            default: bgRes = R.drawable.circle_background; break;
        }
        holder.imgContact.setBackgroundResource(bgRes);

        // عند الضغط على العنصر → فتح شاشة التفاصيل مع startActivityForResult
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailsActivity.class);
            // تمرير بيانات العنصر إلى DetailsActivity
            intent.putExtra("EXTRA_NAME", contact.getName());
            intent.putExtra("EXTRA_MOBILE", contact.getPhone());
            intent.putExtra("EXTRA_COMPANY", contact.getCompany());
            intent.putExtra("EXTRA_NOTES", contact.getNotes());
            intent.putExtra("EXTRA_POSITION", holder.getAdapterPosition());
            intent.putExtra("EXTRA_LETTER", firstLetter);
            intent.putExtra("EXTRA_COLOR_RES", bgRes);

            // فتح شاشة التفاصيل وإرسال النتيجة إلى MainActivity
            ((MainActivity) v.getContext()).startActivityForResult(intent, 200);
        });

        // عند الضغط على زر الاتصال → فتح شاشة الاتصال
        holder.btnCall.setOnClickListener(v -> {
            String phone = contact.getPhone();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            v.getContext().startActivity(intent);
        });
    }

    // عدد العناصر في القائمة
    @Override
    public int getItemCount() {
        return contacts.size();
    }

    // تفعيل الفلترة
    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    // كود الفلترة
    private Filter contactFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                // إذا البحث فارغ → عرض الكل
                filteredList.addAll(contactsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                // فلترة حسب الاسم
                for (Contact item : contactsFull) {
                    if (item.getName().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contacts.clear();
            contacts.addAll((List<Contact>) results.values);
            notifyDataSetChanged(); // تحديث العرض

            // إذا ما في نتائج → إظهار النص
            if (contacts.isEmpty()) {
                txtNoResults.setVisibility(View.VISIBLE);
            } else {
                txtNoResults.setVisibility(View.GONE);
            }
        }
    };

    // ViewHolder داخلي: يربط عناصر الواجهة (النص + الدائرة + زر الاتصال)
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView imgContact; // TextView يمثل الدائرة مع الحرف الأول
        ImageButton btnCall; // زر الاتصال

        public ContactViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgContact = itemView.findViewById(R.id.imgContact);
            btnCall = itemView.findViewById(R.id.btnCall);
        }
    }
}
