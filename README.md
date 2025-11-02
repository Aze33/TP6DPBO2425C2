# Janji

Saya Zahran Zaidan Saputra dengan NIM 2415429 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya, maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

# Desain Program

* **Model:**
    * `Player.java`: Kelas yang menyimpan data/state untuk burung (posisi x, y, ukuran, *velocity*, dan gambar).
    * `Pipe.java`: Kelas yang menyimpan data/state untuk pipa (posisi x, y, ukuran, gambar, dan status `passed`).

* **View:**
    * `View.java`: Kelas yang mewarisi `JPanel` dan bertanggung jawab penuh untuk menggambar semua elemen visual ke layar. Ini termasuk latar belakang, burung, pipa, skor, dan layar "Game Over" beserta tombol-tombolnya.
    * `MenuMenu.java`: Kelas `JDialog` kustom yang bertindak sebagai *view* untuk menu utama.

* **Controller:**
    * `Logic.java`: Kelas utama yang menjadi "otak" dari game. Bertanggung jawab untuk:
        * Mengimplementasikan `ActionListener` untuk *game loop* utama (`Timer`).
        * Mengimplementasikan `KeyListener` untuk menerima input (Spasi, 'R', 'Q').
        * Mengatur `GameState` (MENU, PLAYING, GAME_OVER).
        * Menjalankan semua logika pergerakan, gravitasi, dan deteksi tabrakan.
        * Menghitung dan menyimpan skor.
        * Mengelola *state* game (pause, resume, restart).

* **Main/Entry Point:**
    * `App.java`: Kelas yang memiliki method `main()`. Bertugas untuk:
        * Membuat `JFrame` utama.
        * Menginisialisasi `Logic` dan `View`.
        * Menghubungkan `Logic` dan `View`.
        * Menampilkan `MenuMenu` di awal.

# 2. Penjelasan Alur

Berikut adalah alur program dari awal hingga akhir:

1.  **Eksekusi Awal:**
    * `App.java` dieksekusi, membuat JFrame utama, serta menginisialisasi `Logic` dan `View`.
    * `MenuMenu` (sebuah `JDialog`) langsung ditampilkan. Game dalam status `isPaused = true`.

2.  **Menu Utama:**
    * **User klik "Start Game"**: `MenuMenu` akan memanggil `logic.resumeGame()`, mengubah `isPaused` menjadi `false`, lalu menutup dirinya sendiri (`dispose()`).
    * **User klik "Exit"**: Program akan ditutup (`System.exit(0)`).

3.  **Game Loop (Gameplay):** Selama `gameOver` masih `false`, `gameLoop` akan terus-menerus:
    * Memanggil `move()` untuk menerapkan gravitasi pada burung, menggerakkan pipa, dan mengecek tabrakan.
    * Memanggil `view.repaint()` untuk menggambar ulang semua elemen di layar.
    * Input `Spasi` dari user akan mengubah *velocity* burung agar melompat.

4.  **Game Over:**
    * Jika `move()` mendeteksi tabrakan, `gameOver` di `Logic` disetel ke `true`.
    * `gameLoop` dan `pipesCooldown` dihentikan.
    * `view.showGameOverButtons(true)` dipanggil, memunculkan dua `JButton` di layar.
    * `draw()` di `View` kini juga menggambar teks "GAME OVER".

5.  **Pilihan Setelah Game Over:**
    * **Tekan tombol 'R' / Klik tombol "Press 'R' to Restart"**:
        * `logic.restartGame()` akan dipanggil.
        * Semua state (posisi player, skor, list pipa) di-reset.
        * `gameLoop` dan `pipesCooldown` dimulai kembali.
    * **Tekan tombol 'Q' / Klik tombol "Press 'Q' for Menu"**:
        * `JFrame` saat ini akan ditutup (`dispose()`).
        * `App.main(null)` dipanggil ulang, secara efektif me-restart seluruh aplikasi dan kembali ke Menu Utama (Poin 2).

# Dokumentasi

https://github.com/user-attachments/assets/b7442a09-0c1c-483f-a93e-a59f5ad7cfce




     
