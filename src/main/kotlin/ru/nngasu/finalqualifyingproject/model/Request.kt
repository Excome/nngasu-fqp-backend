package ru.nngasu.finalqualifyingproject.model

import javax.persistence.*

/**
@author Peshekhonov Maksim
 */
@Entity
data class Request(
    @ManyToOne(fetch = FetchType.EAGER)
    var author: User,
    var audience: String,
    @OneToMany(fetch = FetchType.EAGER)
    var equipment: List<Equipment>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    val id: Long = 0
    @ManyToOne
    var responsible: User? = null
    var description: String = ""
    var status: Boolean = false
}
