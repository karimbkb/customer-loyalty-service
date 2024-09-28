package com.kb.customerloyaltyservice.repository

import com.kb.customerloyaltyservice.entity.PointsHistory
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface PointsHistoryRepository : CrudRepository<PointsHistory, UUID>
