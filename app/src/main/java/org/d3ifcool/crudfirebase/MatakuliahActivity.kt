package org.d3ifcool.crudfirebase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*

class MatakuliahActivity : AppCompatActivity() {
    private lateinit var tvNama : TextView
    private lateinit var etMatkul : EditText
    private lateinit var etSks : EditText
    private lateinit var btnMatkul : Button
    private lateinit var lvMatkul : ListView
    private lateinit var matkulList : MutableList<MataKuliah>
    private lateinit var listMatkul : ListView
    private lateinit var ref : DatabaseReference

    companion object{
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_ID = "extra_id"
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matakuliah)

        val id = intent.getStringExtra(EXTRA_ID)
        val nama = intent.getStringExtra(EXTRA_NAMA)

        listMatkul = findViewById(R.id.lv_matkul)
        ref = FirebaseDatabase.getInstance().getReference("matakuliah").child(id!!)

        tvNama = findViewById(R.id.tv_nama)
        etMatkul = findViewById(R.id.et_matkul)
        etSks = findViewById(R.id.et_sks)
        btnMatkul = findViewById(R.id.btn_add)
        lvMatkul = findViewById(R.id.lv_matkul)

        btnMatkul.setOnClickListener { saveMatkul() }
        matkulList = mutableListOf()

        tvNama.text = nama

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    matkulList.clear()
                    for (h in snapshot.children){
                        val matkul = h.getValue(MataKuliah::class.java)
                        if (matkul != null){
                            matkulList.add(matkul)
                        }
                    }

                    val adapter = MataKuliahAdapter(this@MatakuliahActivity, R.layout.item_matkul, matkulList)
                    listMatkul.adapter = adapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun saveMatkul(){
        val namaMatkul = etMatkul.text.toString().trim()
        val sksText = etSks.text.toString()
        val sks = sksText.toInt()

        if (namaMatkul.isEmpty()){
            etMatkul.error = "Matkul Harus Di Isi"
            return
        }

        if (sksText.isEmpty()){
            etSks.error = "Matkul Harus Di Isi"
            return
        }

        val matkulId = ref.push().key
        val matkul = MataKuliah(matkulId!!, namaMatkul, sks)

        if (matkulId != null){
            ref.child(matkulId).setValue(matkul).addOnCompleteListener {
                Toast.makeText(applicationContext, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                etMatkul.text.clear()
                etSks.text.clear()
            }
        }

    }

}