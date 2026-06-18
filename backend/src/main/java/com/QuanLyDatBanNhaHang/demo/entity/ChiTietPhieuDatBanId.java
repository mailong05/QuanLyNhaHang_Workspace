package com.QuanLyDatBanNhaHang.demo.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuDatBanId implements Serializable {

    private String maPhieuDat;
    private String maBan;
}

