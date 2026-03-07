package com.example.contactsapp;

public class Contact {
    // الخصائص (Attributes) الخاصة بجهة الاتصال
    private String name;       // اسم جهة الاتصال
    private String phone;      // رقم الهاتف
    private int imageResId;    // معرف الصورة (Resource ID)
    private String company;    // الشركة
    private String notes;      // الملاحظات

    // كونستركتور كامل (اسم + رقم + صورة + شركة + ملاحظات)
    public Contact(String name, String phone, int imageResId, String company, String notes) {
        this.name = name;
        this.phone = phone;
        this.imageResId = imageResId;
        this.company = company;
        this.notes = notes;
    }

    // كونستركتور (اسم + رقم + صورة فقط)
    public Contact(String name, String phone, int imageResId) {
        this(name, phone, imageResId, "", ""); // استدعاء الكونستركتور الكامل مع قيم فارغة للشركة والملاحظات
    }

    // كونستركتور جديد (اسم + رقم فقط)
    public Contact(String name, String phone) {
        this(name, phone, 0, "", ""); // صورة = 0 (بدون صورة)، شركة وملاحظات فارغة
    }

    // كونستركتور جديد (اسم + رقم + شركة + ملاحظات)
    public Contact(String name, String phone, String company, String notes) {
        this(name, phone, 0, company, notes); // صورة = 0 (بدون صورة)
    }

    // Getter methods (للحصول على القيم)
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getCompany() {
        return company;
    }

    public String getNotes() {
        return notes;
    }

    // Setter methods (لتحديث القيم)
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
