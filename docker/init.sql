IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'QuanLyDatBanNhaHang')
BEGIN
    CREATE DATABASE QuanLyDatBanNhaHang;
    PRINT 'Database QuanLyDatBanNhaHang created successfully.';
END
ELSE
BEGIN
    PRINT 'Database QuanLyDatBanNhaHang already exists.';
END
GO
