package com.latihan.latihansplashscreen

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Mahasiswa(
    val nim: String,
    val nama: String,
    val jurusan: String,
    val semester: Int
)

class DatabaseHelper(context: Context) : 
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    
    companion object {
        private const val DATABASE_NAME = "mahasiswa.db"
        private const val DATABASE_VERSION = 3 // Upgraded to v3
        private const val TABLE_MAHASISWA = "mahasiswa"
        private const val COLUMN_NIM = "nim"
        private const val COLUMN_NAMA = "nama"
        private const val COLUMN_JURUSAN = "jurusan" // New Column
        private const val COLUMN_SEMESTER = "semester" // New Column
    }
    
    override fun onCreate(db: SQLiteDatabase) {
        val createTableMahasiswa = ("CREATE TABLE " + TABLE_MAHASISWA + "("
                + COLUMN_NIM + " TEXT PRIMARY KEY," 
                + COLUMN_NAMA + " TEXT,"
                + COLUMN_JURUSAN + " TEXT,"
                + COLUMN_SEMESTER + " INTEGER" + ")")
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
        contentValues.put(COLUMN_JURUSAN, mahasiswa.jurusan)
        contentValues.put(COLUMN_SEMESTER, mahasiswa.semester)
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
            arrayOf(COLUMN_NIM, COLUMN_NAMA, COLUMN_JURUSAN, COLUMN_SEMESTER),
            null, null, null, null, null
        )
        
        with(cursor) {
            while (moveToNext()) {
                val nim = getString(getColumnIndexOrThrow(COLUMN_NIM))
                val nama = getString(getColumnIndexOrThrow(COLUMN_NAMA))
                val jurusan = getString(getColumnIndexOrThrow(COLUMN_JURUSAN))
                val semester = getInt(getColumnIndexOrThrow(COLUMN_SEMESTER))
                mahasiswaList.add(Mahasiswa(nim, nama, jurusan, semester))
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
            put(COLUMN_JURUSAN, mahasiswa.jurusan)
            put(COLUMN_SEMESTER, mahasiswa.semester)
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
            arrayOf(COLUMN_NIM, COLUMN_NAMA, COLUMN_JURUSAN, COLUMN_SEMESTER),
            "$COLUMN_NIM = ?",
            arrayOf(nim),
            null, null, null
        )
        
        var mahasiswa: Mahasiswa? = null
        if (cursor.moveToFirst()) {
            val nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA))
            val jurusan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JURUSAN))
            val semester = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEMESTER))
            mahasiswa = Mahasiswa(nim, nama, jurusan, semester)
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
