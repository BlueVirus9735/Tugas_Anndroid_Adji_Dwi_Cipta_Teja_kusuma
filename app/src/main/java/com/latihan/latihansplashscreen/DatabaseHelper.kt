package com.latihan.latihansplashscreen

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Mahasiswa(
    val nim: String,
    val nama: String
)

class DatabaseHelper(context: Context) : 
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    
    companion object {
        private const val DATABASE_NAME = "mahasiswa.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_MAHASISWA = "mahasiswa"
        private const val COLUMN_NIM = "nim"
        private const val COLUMN_NAMA = "nama"
    }
    
    override fun onCreate(db: SQLiteDatabase) {
        val createTableMahasiswa = ("CREATE TABLE " + TABLE_MAHASISWA + "("
                + COLUMN_NIM + " TEXT PRIMARY KEY," + COLUMN_NAMA + " TEXT" + ")")
        db.execSQL(createTableMahasiswa)
        
        // Buat tabel pesanan tiket
        val createTableTiket = ("CREATE TABLE pesanan_tiket ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nama_pemesan TEXT,"
                + "jumlah_tiket INTEGER,"
                + "tanggal TEXT" + ")")
        db.execSQL(createTableTiket)
    }
    
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAHASISWA)
        db.execSQL("DROP TABLE IF EXISTS pesanan_tiket")
        onCreate(db)
    }
    
    // CRUD Methods for Mahasiswa
    fun tambahData(mahasiswa: Mahasiswa): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NIM, mahasiswa.nim)
        contentValues.put(COLUMN_NAMA, mahasiswa.nama)
        return db.insert(TABLE_MAHASISWA, null, contentValues)
    }
    
    // CRUD Methods for Tiket
    fun tambahPesananTiket(nama: String, jumlah: Int, tanggal: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nama_pemesan", nama)
        contentValues.put("jumlah_tiket", jumlah)
        contentValues.put("tanggal", tanggal)
        return db.insert("pesanan_tiket", null, contentValues)
    }
    
    // Baca semua data
    fun bacaSemuaData(): List<Mahasiswa> {
        val mahasiswaList = mutableListOf<Mahasiswa>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_MAHASISWA,
            arrayOf(COLUMN_NIM, COLUMN_NAMA),
            null, null, null, null, null
        )
        
        with(cursor) {
            while (moveToNext()) {
                val nim = getString(getColumnIndexOrThrow(COLUMN_NIM))
                val nama = getString(getColumnIndexOrThrow(COLUMN_NAMA))
                mahasiswaList.add(Mahasiswa(nim, nama))
            }
            close()
        }
        return mahasiswaList
    }
    
    // Update data
    fun ubahData(mahasiswa: Mahasiswa): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAMA, mahasiswa.nama)
        }
        return db.update(
            TABLE_MAHASISWA,
            values,
            "$COLUMN_NIM = ?",
            arrayOf(mahasiswa.nim)
        )
    }
    
    // Cari data berdasarkan NIM
    fun cariDataByNim(nim: String): Mahasiswa? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MAHASISWA,
            arrayOf(COLUMN_NIM, COLUMN_NAMA),
            "$COLUMN_NIM = ?",
            arrayOf(nim),
            null, null, null
        )
        
        var mahasiswa: Mahasiswa? = null
        if (cursor.moveToFirst()) {
            val nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA))
            mahasiswa = Mahasiswa(nim, nama)
        }
        cursor.close()
        return mahasiswa
    }
    
    // Hapus data
    fun hapusData(nim: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_MAHASISWA, "$COLUMN_NIM = ?", arrayOf(nim))
    }
}
