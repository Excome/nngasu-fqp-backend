package ru.nngasu.finalqualifyingproject.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.nngasu.finalqualifyingproject.exception.RequestException
import ru.nngasu.finalqualifyingproject.exception.error.RequestError
import ru.nngasu.finalqualifyingproject.model.Equipment
import ru.nngasu.finalqualifyingproject.model.Request
import ru.nngasu.finalqualifyingproject.repository.RequestRepository
import kotlin.streams.toList

/**
@author Peshekhonov Maksim
 */
@Service
class RequestService {
    @Autowired
    lateinit var requestRepository: RequestRepository
    @Autowired
    lateinit var equipmentService: EquipmentService
    @Autowired
    lateinit var userService: UserService

    fun getRequestById(id: Long): Request {
        return requestRepository.findRequestById(id)
            ?: throw RequestException("Request  with '$id' id not found", RequestError.REQUEST_NOT_FOUND)
    }

    fun getRequests(pageable: Pageable): List<Request>{
        val list = requestRepository.findAll(pageable).content
        if (list.isEmpty())
            throw RequestException("Requests on '${pageable.pageNumber} page not found'", RequestError.REQUEST_NOT_FOUND)
        return list
    }

    fun getRequestByAuthor(userName: String, pageable: Pageable): List<Request> {
        val list = requestRepository.findAllByAuthorUserName(userName, pageable).content
        if (list.isEmpty())
            throw RequestException("Requests with '$userName' author on '${pageable.pageNumber} page not found'", RequestError.REQUEST_NOT_FOUND)

        return list
    }

    fun getRequestsByResponsible(userName: String, pageable: Pageable): List<Request> {
        val list = requestRepository.findAllByResponsibleUserName(userName, pageable).content
        if (list.isEmpty())
            throw RequestException("Requests with '$userName' responsible on '${pageable.pageNumber} page not found'", RequestError.REQUEST_NOT_FOUND)

        return list
    }

    fun createRequest(request: Request): Request {
        val author = userService.getUserByUserName(request.author.userName)

        request.author = author
        for (i in request.equipment.indices) {
            val equipmentByName = equipmentService.getEquipmentByName(request.equipment[i].name)
            request.equipment[i] = equipmentByName
        }

        return requestRepository.save(request)
    }

    fun changeRequest(request: Request): Request {
        val requestFromDb = requestRepository.findByIdOrNull(request.id)
            ?: throw RequestException("Request  with '${request.id}' id not found", RequestError.REQUEST_NOT_FOUND)

        requestFromDb.author = userService.getUserByUserName(request.author.userName)
        if (request.responsible != null)
            requestFromDb.responsible = userService.getUserByUserName(request.responsible!!.userName)
        else
            requestFromDb.responsible = null
        var equipments = mutableListOf<Equipment>();
        for (item in request.equipment){
            equipments.add(equipmentService.getEquipmentByName(item.name));
        }

        requestFromDb.equipment = equipments;
        requestFromDb.audience = request.audience
        requestFromDb.description = request.description
        requestFromDb.status = request.status

        return requestRepository.save(requestFromDb)
    }

    fun changeStatus(id: Long, status: Boolean): Request {
        val request = getRequestById(id)
        request.status = status
        return requestRepository.save(request)
    }

    fun assignRequest(id: Long, request: Request): Request {
        val requestFromDb = getRequestById(id)
        requestFromDb.responsible = request.responsible
        return requestRepository.save(requestFromDb)
    }

    fun removeRequest(id: Long) {
        val request = getRequestById(id)
        requestRepository.delete(request)
    }
}