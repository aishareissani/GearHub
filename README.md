GearHub
GearHub adalah aplikasi peer-to-peer (P2P) untuk menyewa dan menyewakan perlengkapan kamera, lensa, audio, dan lighting bagi mahasiswa, freelancer, dan content creator yang membutuhkan peralatan tanpa harus membelinya. Aplikasi ini memiliki fitur login dan daftar, beranda, pencarian dan filter gear, pemilihan lokasi, detail gear, pengajuan sewa, pembayaran, serta ringkasan transaksi.

Fitur yang Dikembangkan
- Autentikasi: Login dan registrasi
- Beranda: Menampilkan perlengkapan gear yang tersedia dan informasi lokasi
- Pencarian: Mencari gear dan memfilter berdasarkan kategori
- Detail Gear: Menampilkan informasi gear
- Ajukan Sewa: Memilih tanggal sewa dan melihat ringkasan biaya
- Pembayaran: Memilih metode pembayaran dan melihat ringkasan transaksi
- UI fitur native: Pilih Lokasi untuk menampilkan dan memilih lokasi gear terdekat (di Beranda dan Pencarian)

Nama kelompok: kebetulan berempat
Nama anggota:
1. Amanda Aliyah (00000135964)
2. Aisha Reissani Sopyan (00000136172)
3. Kenny Valent Winalda Sembiring (00000136177)
4. Vaurolla Az Zahra (00000139045)

Pembagian tugas
1. Amanda Aliyah (00000135964)
- Membuat struktur navigasi aplikasi (NavHost, NavController, dan Routes)
- Mengimplementasikan halaman Login dan Daftar
- Membuat komponen form autentikasi
- Membuat validasi nama, email kampus, dan password
- Mengelola state pengguna melalui AuthViewModel
- Menyiapkan dummy data pengguna

2. Aisha Reissani Sopyan (00000136172)
- Membuat tema aplikasi menggunakan Material 3, meliputi warna, typography, dan shapes
- Mengimplementasikan halaman Beranda
- Membuat komponen GearCard dan BottomNavBar
- Membuat halaman Pilih Lokasi beserta state lokasi terpilih
- Mengelola kategori gear dan helper inisial nama

3. Kenny Valent Winalda Sembiring (00000136177)
- Membuat model dan dummy data gear beserta data booking
- Mengimplementasikan halaman Pencarian dan filter kategori
- Membuat halaman Detail Gear dengan informasi, galeri foto, dan spesifikasi
- Membuat dialog kalender ketersediaan gear
- Mengelola logika ketersediaan tanggal serta helper format tanggal dan Rupiah

4. Vaurolla Az Zahra (00000139045)
- Mengimplementasikan halaman Ajukan Sewa dengan pemilihan tanggal dan input nomor telepon
- Membuat RentViewModel, RentUiState, dan validasi sewa
- Menghitung durasi sewa, biaya layanan, deposit, dan total pembayaran
- Membuat halaman Pembayaran beserta pilihan metode pembayaran
- Mengimplementasikan halaman Sukses dengan ringkasan transaksi
- Menyiapkan dummy data metode pembayaran dan utilitas tanggal

Sistem Kerja Kelompok
Kelompok membagi tugas berdasarkan fitur dan tanggung jawab masing-masing anggota dengan mengikuti alur pengembangan aplikasi GearHub, mulai dari fondasi aplikasi dan autentikasi, tampilan beranda, pencarian dan detail gear, hingga proses penyewaan dan pembayaran. Setiap anggota bertanggung jawab atas implementasi fitur yang telah dibagi. Integrasi dilakukan dengan menyelaraskan struktur navigasi, model data, dan komponen UI agar seluruh fitur dapat berjalan sebagai satu aplikasi. Kelompok juga melakukan koordinasi dan pengecekan bersama untuk memastikan fitur terintegrasi dengan baik.
