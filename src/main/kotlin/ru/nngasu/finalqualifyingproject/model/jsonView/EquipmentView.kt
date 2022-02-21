package ru.nngasu.finalqualifyingproject.model.jsonView

/**
 * This marker interfaces for set serialization view of Equipment to Json
 * @author Peshekhonov Maksim
 */
class EquipmentView {
    interface Common: ErrorView.Info
    interface All: Common
}