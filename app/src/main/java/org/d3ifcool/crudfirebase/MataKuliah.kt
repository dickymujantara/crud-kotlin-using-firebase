package org.d3ifcool.crudfirebase

data class MataKuliah(
    val id : String,
    val nama : String,
    val sks : Int
){
    constructor(): this("","",0){

    }
}