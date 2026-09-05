package com.QuanLyDatBanNhaHang.demo.enums;

public enum DonViTinhMonAn {
    DIA("Đĩa"),
    PHAN("Phần"),
    LY("Ly"),
    NOI("Nồi"),
    CHAI("Chai"),
    LON("Lon"),
    CUON("Cuốn"),
    TO("Tô"),
    KHAC("Khác");

    private final String tenHienThi;

    DonViTinhMonAn(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }
}
