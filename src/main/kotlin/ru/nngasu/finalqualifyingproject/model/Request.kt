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
    var description: String = ""
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0
    @ManyToOne
    lateinit var responsible: User
    var status: Boolean = false
}
