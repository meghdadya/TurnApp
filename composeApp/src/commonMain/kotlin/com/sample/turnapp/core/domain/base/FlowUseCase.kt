package com.sample.turnapp.core.domain.base

import com.sample.turnapp.core.domain.model.DomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError
import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<Param, out Result: DomainModel> : BaseUseCase {
    abstract suspend operator fun invoke(param: Param): Flow<Resource<Result, GeneralError>>
}