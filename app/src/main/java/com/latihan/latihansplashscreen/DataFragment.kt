package com.latihan.latihansplashscreen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class DataFragment : Fragment() {
    
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var etNim: TextInputEditText
    private lateinit var etNama: TextInputEditText
    private lateinit var etJurusan: TextInputEditText
    private lateinit var etSemester: TextInputEditText
    private lateinit var btnTambah: Button
    private lateinit var lvMahasiswa: ListView
    
    private lateinit var listAdapter: ArrayAdapter<String>
    private var mahasiswaList: List<Mahasiswa> = ArrayList()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())
        
        // Init Views
        etNim = view.findViewById(R.id.et_nim)
        etNama = view.findViewById(R.id.et_nama)
        etJurusan = view.findViewById(R.id.et_jurusan)
        etSemester = view.findViewById(R.id.et_semester)
        btnTambah = view.findViewById(R.id.btn_tambah)
        lvMahasiswa = view.findViewById(R.id.lv_mahasiswa)
        
        // Animation References
        val cardInput = view.findViewById<View>(R.id.card_input)
        val cardResult = view.findViewById<View>(R.id.card_result)
        
        // Setup simple animations
        cardInput.alpha = 0f
        cardInput.translationY = 50f
        cardResult.alpha = 0f
        cardResult.translationY = 50f
        cardInput.animate().alpha(1f).translationY(0f).setDuration(500).start()
        cardResult.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(100).start()
        
        // Listeners
        btnTambah.setOnClickListener { tambahData() }
        
        // Setup ListView Click
        lvMahasiswa.setOnItemClickListener { _, _, position, _ ->
            val selectedMahasiswa = mahasiswaList[position]
            showEditDialog(selectedMahasiswa)
        }
        
        // Load Data
        refreshList()
    }
    
    private fun tambahData() {
        val nim = etNim.text.toString().trim()
        val nama = etNama.text.toString().trim()
        val jurusan = etJurusan.text.toString().trim()
        val semesterStr = etSemester.text.toString().trim()
        
        if (nim.isEmpty() || nama.isEmpty() || jurusan.isEmpty() || semesterStr.isEmpty()) {
            Toast.makeText(requireContext(), "Semua data harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }
        
        val semester = semesterStr.toIntOrNull() ?: 1
        
        val mahasiswa = Mahasiswa(nim, nama, jurusan, semester)
        val result = databaseHelper.tambahData(mahasiswa)
        
        if (result > 0) {
            Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
            clearInputs()
            refreshList()
        } else {
            Toast.makeText(requireContext(), "Gagal simpan (NIM mungkin duplikat)", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showEditDialog(mahasiswa: Mahasiswa) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_data, null)
        
        val etDialogNim = dialogView.findViewById<TextInputEditText>(R.id.et_dialog_nim)
        val etDialogNama = dialogView.findViewById<TextInputEditText>(R.id.et_dialog_nama)
        val etDialogJurusan = dialogView.findViewById<TextInputEditText>(R.id.et_dialog_jurusan)
        val etDialogSemester = dialogView.findViewById<TextInputEditText>(R.id.et_dialog_semester)
        
        // Set values
        etDialogNim.setText(mahasiswa.nim)
        etDialogNama.setText(mahasiswa.nama)
        etDialogJurusan.setText(mahasiswa.jurusan)
        etDialogSemester.setText(mahasiswa.semester.toString())
        
        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                // Update Logic
                val baruNama = etDialogNama.text.toString().trim()
                val baruJurusan = etDialogJurusan.text.toString().trim()
                val baruSemester = etDialogSemester.text.toString().trim().toIntOrNull() ?: 1
                
                if (baruNama.isNotEmpty()) {
                    val updatedMahasiswa = Mahasiswa(mahasiswa.nim, baruNama, baruJurusan, baruSemester)
                    databaseHelper.ubahData(updatedMahasiswa)
                    refreshList()
                    Toast.makeText(requireContext(), "Data diperbarui", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hapus") { _, _ ->
                // Delete Logic
                databaseHelper.hapusData(mahasiswa.nim)
                refreshList()
                Toast.makeText(requireContext(), "Data dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNeutralButton("Batal", null)
            .show()
    }
    
    private fun refreshList() {
        mahasiswaList = databaseHelper.bacaSemuaData()
        
        val displayList = mahasiswaList.map { 
            "${it.nama} (${it.jurusan} - Sem ${it.semester})\nNIM: ${it.nim}" 
        }
        
        listAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, displayList)
        lvMahasiswa.adapter = listAdapter
    }
    
    private fun clearInputs() {
        etNim.text?.clear()
        etNama.text?.clear()
        etJurusan.text?.clear()
        etSemester.text?.clear()
        etNim.requestFocus()
    }
}
