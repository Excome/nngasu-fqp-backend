package ru.nngasu.finalqualifyingproject.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import ru.nngasu.finalqualifyingproject.exception.RequestException
import ru.nngasu.finalqualifyingproject.exception.error.RequestError
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
        return requestRepository.findByIdOrNull(id)
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
        request.equipment = request.equipment.stream()
            .map { equipment ->  equipmentService.getEquipmentByName(equipment.name)}.toList()

        return requestRepository.save(request)
    }

    fun changeRequest(request: Request): Request {
        val requestFromDb = requestRepository.findByIdOrNull(request.id)
            ?: throw RequestException("Request  with '${request.id}' id not found", RequestError.REQUEST_NOT_FOUND)

        requestFromDb.author = request.author
        requestFromDb.responsible = request.responsible
        requestFromDb.equipment = request.equipment
        requestFromDb.audience = request.audience
        requestFromDb.description = request.description
        requestFromDb.status = request.status

        return requestRepository.save(requestFromDb)
    }

    fun removeRequest(id: Long) {
        val request = getRequestById(id)
        requestRepository.delete(request)
    }
}