package ru.nngasu.finalqualifyingproject.model

import com.fasterxml.jackson.annotation.JsonView
import ru.nngasu.finalqualifyingproject.model.jsonView.RequestView
import javax.persistence.*

/**
@author Peshekhonov Maksim
 */
@Entity
class Request{
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonView(RequestView.CommonView::class)
    lateinit var author: User
    @JsonView(RequestView.CommonView::class)
    var audience: String = ""
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonView(RequestView.All::class)
    var equipment: MutableList<Equipment> = mutableListOf()
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @JsonView(RequestView.CommonView::class)
    val id: Long = 0
    @ManyToOne
    @JsonView(RequestView.CommonView::class)
    var responsible: User? = null
    @JsonView(RequestView.All::class)
    var description: String = ""
    @JsonView(RequestView.CommonView::class)
    var status: Boolean = false
}
