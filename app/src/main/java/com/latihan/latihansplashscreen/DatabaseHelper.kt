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
        private const val DATABASE_VERSION = 1
        private const val TABLE_MAHASISWA = "mahasiswa"
        private const val COLUMN_NIM = "nim"
        private const val COLUMN_NAMA = "nama"
    }
    
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_MAHASISWA (
                $COLUMN_NIM TEXT PRIMARY KEY,
                $COLUMN_NAMA TEXT
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }
    
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MAHASISWA")
        onCreate(db)
    }
    
    // Tambah data
    fun tambahData(mahasiswa: Mahasiswa): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NIM, mahasiswa.nim)
            put(COLUMN_NAMA, mahasiswa.nama)
        }
        return db.insert(TABLE_MAHASISWA, null, values)
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
