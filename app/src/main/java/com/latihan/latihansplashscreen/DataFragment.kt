package com.latihan.latihansplashscreen

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class DataFragment : Fragment() {
    
    private lateinit var sessionManager: SessionManager
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editTextNim: EditText
    private lateinit var editTextNama: EditText
    private lateinit var buttonTambahData: Button
    private lateinit var buttonUbahData: Button
    private lateinit var buttonCariData: Button
    private lateinit var buttonHapusData: Button
    private lateinit var textViewHasilData: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data, container, false)
        
        // Initialize helpers
        sessionManager = SessionManager(requireContext())
        databaseHelper = DatabaseHelper(requireContext())
        
        // Initialize views
        editTextNim = view.findViewById(R.id.edit_text_nim)
        editTextNama = view.findViewById(R.id.edit_text_nama)
        buttonTambahData = view.findViewById(R.id.button_tambah_data)
        buttonUbahData = view.findViewById(R.id.button_ubah_data)
        buttonCariData = view.findViewById(R.id.button_cari_data)
        buttonHapusData = view.findViewById(R.id.button_hapus_data)
        textViewHasilData = view.findViewById(R.id.text_view_hasil_data)
        
        // Apply theme
        applyTheme(view)
        
        // Set button listeners
        buttonTambahData.setOnClickListener { tambahData() }
        buttonUbahData.setOnClickListener { ubahData() }
        buttonCariData.setOnClickListener { cariData() }
        buttonHapusData.setOnClickListener { hapusData() }
        
        // Load all data initially
        tampilkanSemuaData()
        
        return view
    }
    
    private fun tambahData() {
        val nim = editTextNim.text.toString().trim()
        val nama = editTextNama.text.toString().trim()
        
        if (nim.isEmpty() || nama.isEmpty()) {
            Toast.makeText(requireContext(), "NIM dan Nama harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }
        
        val mahasiswa = Mahasiswa(nim, nama)
        val result = databaseHelper.tambahData(mahasiswa)
        
        if (result > 0) {
            Toast.makeText(requireContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            clearInputs()
            tampilkanSemuaData()
        } else {
            Toast.makeText(requireContext(), "Gagal menambahkan data", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun ubahData() {
        val nim = editTextNim.text.toString().trim()
        val nama = editTextNama.text.toString().trim()
        
        if (nim.isEmpty() || nama.isEmpty()) {
            Toast.makeText(requireContext(), "NIM dan Nama harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }
        
        val mahasiswa = Mahasiswa(nim, nama)
        val result = databaseHelper.ubahData(mahasiswa)
        
        if (result > 0) {
            Toast.makeText(requireContext(), "Data berhasil diubah", Toast.LENGTH_SHORT).show()
            clearInputs()
            tampilkanSemuaData()
        } else {
            Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun cariData() {
        val nim = editTextNim.text.toString().trim()
        
        if (nim.isEmpty()) {
            Toast.makeText(requireContext(), "NIM harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }
        
        val mahasiswa = databaseHelper.cariDataByNim(nim)
        
        if (mahasiswa != null) {
            editTextNama.setText(mahasiswa.nama)
            textViewHasilData.text = "Data ditemukan:\nNIM: ${mahasiswa.nim}\nNama: ${mahasiswa.nama}"
        } else {
            Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun hapusData() {
        val nim = editTextNim.text.toString().trim()
        
        if (nim.isEmpty()) {
            Toast.makeText(requireContext(), "NIM harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }
        
        val result = databaseHelper.hapusData(nim)
        
        if (result > 0) {
            Toast.makeText(requireContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            clearInputs()
            tampilkanSemuaData()
        } else {
            Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun tampilkanSemuaData() {
        val mahasiswaList = databaseHelper.bacaSemuaData()
        
        if (mahasiswaList.isEmpty()) {
            textViewHasilData.text = "Belum ada data mahasiswa"
        } else {
            val sb = StringBuilder("Daftar Mahasiswa:\n\n")
            mahasiswaList.forEach { mahasiswa ->
                sb.append("NIM: ${mahasiswa.nim}\n")
                sb.append("Nama: ${mahasiswa.nama}\n")
                sb.append("---\n")
            }
            textViewHasilData.text = sb.toString()
        }
    }
    
    private fun clearInputs() {
        editTextNim.text.clear()
        editTextNama.text.clear()
    }
    
    private fun applyTheme(view: View) {
        val isDarkMode = sessionManager.isDarkMode()
        if (isDarkMode) {
            textViewHasilData.setTextColor(Color.WHITE)
        } else {
            textViewHasilData.setTextColor(Color.BLACK)
        }
    }
}
