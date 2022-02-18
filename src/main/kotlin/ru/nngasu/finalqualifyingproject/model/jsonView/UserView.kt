package ru.nngasu.finalqualifyingproject.model.jsonView

/**
 *  This marker interfaces for set serialization view of User to Json
 *  @author Peshekhonov Maksim
 */
class UserView {
    interface Common: RoleView.Name
    interface Profile: Common
}