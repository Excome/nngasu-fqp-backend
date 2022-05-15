package ru.nngasu.finalqualifyingproject.model

import com.fasterxml.jackson.annotation.JsonView
import ru.nngasu.finalqualifyingproject.model.jsonView.EquipmentView
import javax.persistence.*

/**
@author Peshekhonov Maksim
*/
@Entity
@Table(name = "equipment")
class Equipment{
    @JsonView(EquipmentView.Common::class)
    var name: String = ""
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(EquipmentView.All::class)
    var id: Long = 0
    @JsonView(EquipmentView.Common::class)
    var count: Int = 0
    @JsonView(EquipmentView.Common::class)
    var type: String = ""
    @JsonView(EquipmentView.Common::class)
    var description: String = ""
}
