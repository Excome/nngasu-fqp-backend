package ru.nngasu.finalqualifyingproject.exception.error

/**
@author Peshekhonov Maksim
 */
open interface Error {
    val code: Int
    val message: String
}