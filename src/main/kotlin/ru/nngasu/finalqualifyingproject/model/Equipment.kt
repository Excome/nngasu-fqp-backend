package ru.nngasu.finalqualifyingproject.model

import javax.persistence.*

/**
@author Peshekhonov Maksim
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
}
