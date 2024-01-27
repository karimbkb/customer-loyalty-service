package com.kb.customerloyaltyservice.dto

import com.kb.customerloyaltyservice.enums.LoyaltyType
import com.kb.customerloyaltyservice.enums.TransactionType
import io.swagger.annotations.ApiModelProperty
import java.util.UUID
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

data class PointsHistoryCreateDTO(
    @ApiModelProperty(
        value = "Customer ID for which user to create points",
        example = "29303a37-945d-4fba-bba9-74a575e66a4b",
        dataType = "UUID",
    )
    @field:NotNull
    val customerId: UUID,
    @ApiModelProperty(
        value = "Amount of points to add/subtract from customer",
        example = "34",
        dataType = "Integer",
    )
    @field:PositiveOrZero
    val points: Int,
    @ApiModelProperty(
        value = "The transaction type for that points record",
        example = "ADD",
        allowableValues = "ADD, SUBTRACT",
        dataType = "TransactionType",
    )
    @field:NotNull
    val transactionType: TransactionType,
    @ApiModelProperty(
        value = "The loyalty type for that points record",
        example = "ORDER",
        allowableValues = "ORDER, ORDER_CANCELLED, MANUAL_ENTRY",
        dataType = "LoyaltyType",
    )
    @field:NotNull
    val loyaltyType: LoyaltyType,
    @ApiModelProperty(
        value = "Reason for adding/subtracting points",
        example = "Order with number #436272986",
        dataType = "String",
    )
    @field:NotNull
    @field:NotEmpty
    val reason: String,
)
