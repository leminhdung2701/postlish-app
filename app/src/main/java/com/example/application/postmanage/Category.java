package com.example.application.postmanage;

public class Category {
    /* CATEGORY_001("CATEGORY_001","Chính trị – pháp luật"),
     CATEGORY_002("CATEGORY_002","Kinh tế"),
     CATEGORY_003("CATEGORY_003","Lí luận, triết học"),
     CATEGORY_004("CATEGORY_004","Khoa học công nghệ"),
     CATEGORY_005("CATEGORY_005","Văn học nghệ thuật"),
     CATEGORY_006("CATEGORY_006","Văn hóa xã hội – Lịch sử"),
     CATEGORY_007("CATEGORY_007","Giáo dục"),
     CATEGORY_008("CATEGORY_008","Tâm lý , Tâm linh , Tôn giáo "),
     CATEGORY_009("CATEGORY_009","Ẩm thực"),
     CATEGORY_010("CATEGORY_010","Âm nhạc"),
     CATEGORY_011("CATEGORY_011","Y dược"),
     CATEGORY_012("CATEGORY_012","Mỹ thuật"),
     CATEGORY_013("CATEGORY_013","Nông nghiệp"); */
    String category1;
    String category2;
    String category3;
    public Category(){

    }
    public Category(String category1, String category2, String category3) {
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }
}