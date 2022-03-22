package ru.nngasu.finalqualifyingproject.model.jsonView

/**
 * This marker interfaces for set serialization view of Request to Json
 * @author Peshekhonov Maksim
 */
class RequestView {
    interface CommonView: UserView.Common, EquipmentView.Common
    interface All: CommonView
}