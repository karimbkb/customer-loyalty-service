package com.kb.customerloyaltyservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc

@EnableSwagger2WebMvc
@SpringBootApplication
class CustomerLoyaltyServiceApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<CustomerLoyaltyServiceApplication>(*args)
}
