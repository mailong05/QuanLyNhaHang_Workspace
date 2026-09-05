package com.QuanLyDatBanNhaHang.demo.enums;

public enum LoaiMonAn {
    MON_KHAI_VI("Món khai vị"),
    MON_CHINH("Món chính"),
    MON_TRANG_MIENG("Món tráng miệng"),
    DO_UONG("Đồ uống"),
    LAU("Lẩu"),
    NUONG("Nướng"),
    COM_MIE_CHAO("Cơm/Mì/Cháo"),
    KHAC("Khác");

    private final String tenHienThi;

    LoaiMonAn(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }
}
