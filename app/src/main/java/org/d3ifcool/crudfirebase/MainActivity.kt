package org.d3ifcool.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var etNama : EditText
    private lateinit var etAlamat : EditText
    private lateinit var btnSave : Button
    private lateinit var ref : DatabaseReference
    private lateinit var listMhs : ListView
    private lateinit var mhsList : MutableList<Mahasiswa>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ref = FirebaseDatabase.getInstance().getReference("mahasiswa")

        etNama = findViewById(R.id.et_nama)
        etAlamat = findViewById(R.id.et_alamat)
        btnSave = findViewById(R.id.btn_save)
        btnSave.setOnClickListener{saveData()}

        listMhs = findViewById(R.id.lv_mhs)
        mhsList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    mhsList.clear()
                    for (h  in snapshot.children){
                        val mahasiswa = h.getValue(Mahasiswa::class.java)
                        if (mahasiswa != null){
                            mhsList.add(mahasiswa)
                        }
                    }

                    val adapter = MahasiswaAdapter(this@MainActivity, R.layout.item_mhs, mhsList)
                    listMhs.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        listMhs.setOnItemClickListener { adapterView, view, i, l ->
            val mahasiswa = mhsList.get(i)

            val intent = Intent(this@MainActivity, MatakuliahActivity::class.java)
            intent.putExtra(MatakuliahActivity.EXTRA_ID, mahasiswa.id)
            intent.putExtra(MatakuliahActivity.EXTRA_NAMA, mahasiswa.nama)

            startActivity(intent)
        }

    }

    private fun saveData(){
        val nama = etNama.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()

        if (nama.isEmpty()){
            etNama.error = "Isi Nama"
        }

        if (alamat.isEmpty()){
            etAlamat.error = "Isi Alamat"
        }

        val mhsId = ref.push().key

        val mhs = Mahasiswa(mhsId,nama,alamat)

        if (mhsId != null){
            ref.child(mhsId).setValue(mhs).addOnCompleteListener {
                Toast.makeText(applicationContext, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                etNama.text.clear()
                etAlamat.text.clear()
            }
        }
    }
}