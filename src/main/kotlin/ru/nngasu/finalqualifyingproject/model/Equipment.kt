package ru.nngasu.finalqualifyingproject.model

import javax.persistence.*

/**
@author Lalykin Kirill
*/
@Entity
@Table(name = "equipment")
data class Equipment(
    var name: String,
    var count: Int,
    var type: String,
    var description: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    fun getEquipmentName(): String {
        return this.name
    }

    fun getEquipmentCount(): Int {
        return this.count
    }

    fun getEquipmentType(): String {
        return this.type
    }

    fun getEquipmentDescription(): String{
        return this.description
    }
}
