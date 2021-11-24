package ru.nngasu.finalqualifyingproject.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
@author Peshekhonov Maksim
*/
@Entity
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
