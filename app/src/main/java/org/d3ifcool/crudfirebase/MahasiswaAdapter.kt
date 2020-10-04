package org.d3ifcool.crudfirebase

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class MahasiswaAdapter(val mCtx : Context, val layoutResId : Int, val mhsList : List<Mahasiswa>) :
    ArrayAdapter<Mahasiswa>(mCtx,layoutResId, mhsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutResId, null)

        val tvNama : TextView = view.findViewById(R.id.tv_nama)
        val tvAlamat : TextView = view.findViewById(R.id.tv_alamat)
        val tvEdit : TextView = view.findViewById(R.id.tv_edit)

        val mahasiswa = mhsList[position]

        tvEdit.setOnClickListener {
            showUpdateDialog(mahasiswa)
        }

        tvNama.text = mahasiswa.nama
        tvAlamat.text = mahasiswa.alamat

        return view
    }

    fun showUpdateDialog(mhs : Mahasiswa){
        val builder = AlertDialog.Builder(mCtx)
        val db = FirebaseDatabase.getInstance()

        builder.setTitle("Edit Data Mahasiwa")

        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.update_dialog, null)

        val etNama : TextView = view.findViewById(R.id.et_nama)
        val etAlamat : TextView = view.findViewById(R.id.et_alamat)

        etNama.text = mhs.nama
        etAlamat.text = mhs.alamat

        builder.setView(view)

        builder.setPositiveButton("Update"){p0, p1 ->
            val nama = etNama.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()
            if (nama.isEmpty()){
                etNama.error = "Isi Nama"
                etNama.requestFocus()
                return@setPositiveButton
            }

            if (alamat.isEmpty()){
                etAlamat.error = "Isi Alamat"
                etAlamat.requestFocus()
                return@setPositiveButton
            }

            val mahasiswa = Mahasiswa(mhs.id, nama, alamat)
            db.getReference("mahasiswa").child(mhs.id!!).setValue(mahasiswa)

            Toast.makeText(mCtx, "Data Berhasil Di Update", Toast.LENGTH_SHORT).show()

        }

        builder.setNeutralButton("Cancel"){p0,p1 ->

        }

        builder.setNegativeButton("Delete"){p0, p1 ->
            db.getReference("mahasiswa").child(mhs.id!!).removeValue()
            db.getReference("matakuliah").child(mhs.id!!).removeValue()

            Toast.makeText(mCtx, "Data Berhasil Di Hapus", Toast.LENGTH_SHORT).show()
        }

        val alert = builder.create()
        alert.show()
    }

}