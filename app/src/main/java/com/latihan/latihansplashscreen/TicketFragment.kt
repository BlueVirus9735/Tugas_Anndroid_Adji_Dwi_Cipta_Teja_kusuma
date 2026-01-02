package com.latihan.latihansplashscreen

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TicketFragment : Fragment() {

    private lateinit var etNama: TextInputEditText
    private lateinit var etJumlah: TextInputEditText
    private lateinit var btnPesan: Button
    private lateinit var databaseHelper: DatabaseHelper

    companion object {
        const val CHANNEL_ID = "ticket_channel"
        const val PERMISSION_REQUEST_CODE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())
        etNama = view.findViewById(R.id.et_nama_pemesan)
        etJumlah = view.findViewById(R.id.et_jumlah_tiket)
        btnPesan = view.findViewById(R.id.btn_pesan_tiket)
        val cardForm = view.findViewById<View>(R.id.card_form)
        val tvTitle = view.findViewById<View>(R.id.tv_title)
        val tvSubtitle = view.findViewById<View>(R.id.tv_subtitle)

        createNotificationChannel()
        
        // Entrance Animation
        cardForm.alpha = 0f
        cardForm.translationY = 100f
        cardForm.animate().alpha(1f).translationY(0f).setDuration(500).setStartDelay(100).start()
        
        tvTitle.alpha = 0f
        tvTitle.translationY = -50f
        tvTitle.animate().alpha(1f).translationY(0f).setDuration(500).start()
        
        tvSubtitle.alpha = 0f
        tvSubtitle.animate().alpha(1f).setDuration(500).setStartDelay(200).start()

        btnPesan.setOnClickListener {
            handlePesanTiket()
        }
    }

    private fun handlePesanTiket() {
        val nama = etNama.text.toString().trim()
        val jumlahStr = etJumlah.text.toString().trim()

        if (nama.isEmpty()) {
            etNama.error = "Nama tidak boleh kosong"
            return
        }

        if (jumlahStr.isEmpty()) {
            etJumlah.error = "Jumlah tiket tidak boleh kosong"
            return
        }

        val jumlah = jumlahStr.toIntOrNull()
        if (jumlah == null || jumlah <= 0) {
            etJumlah.error = "Jumlah tiket tidak valid"
            return
        }

        // Cek Permission Notifikasi untuk Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
                return
            }
        }

        // Tampilkan Loading
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Sedang memproses pesanan...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        // Simulasi proses 2 detik
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            simpanKeDatabase(nama, jumlah)
        }, 2000)
    }

    private fun simpanKeDatabase(nama: String, jumlah: Int) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val result = databaseHelper.tambahPesananTiket(nama, jumlah, currentDate)

        if (result != -1L) {
            showNotification(nama, jumlah)
            resetForm()
        } else {
            Toast.makeText(requireContext(), "Gagal menyimpan pesanan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNotification(nama: String, jumlah: Int) {
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("show_dialog", true)
            putExtra("nama_pemesan", nama)
            putExtra("jumlah_tiket", jumlah)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_ticket) // Pastikan icon ada
            .setContentTitle("Pesanan Berhasil")
            .setContentText("Tiket atas nama $nama berhasil dipesan")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            with(NotificationManagerCompat.from(requireContext())) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Gagal menampilkan notifikasi: Izin ditolak", Toast.LENGTH_SHORT).show()
        }
        
        // Cadangan jika notifikasi tidak muncul (misal di emulator lama tanpa Google Play Services update)
        Toast.makeText(requireContext(), "Tiket atas nama $nama berhasil dipesan!", Toast.LENGTH_LONG).show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Ticket Channel"
            val descriptionText = "Channel for ticket booking notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun resetForm() {
        etNama.text?.clear()
        etJumlah.text?.clear()
        etNama.clearFocus()
        etJumlah.clearFocus()
    }
}
