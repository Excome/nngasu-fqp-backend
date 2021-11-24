package ru.nngasu.finalqualifyingproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class FinalQualifyingProjectApplication


fun main(args: Array<String>) {
	runApplication<FinalQualifyingProjectApplication>(*args)
}
