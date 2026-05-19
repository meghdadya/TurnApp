package com.sample.turnapp.core.domain.base

import com.sample.turnapp.core.domain.model.DomainModel
import com.sample.turnapp.core.domain.model.Resource
import com.sample.turnapp.core.domain.model.error.GeneralError

abstract class UseCase<Param, out Result: DomainModel> : BaseUseCase {
    abstract suspend operator fun invoke(param: Param): Resource<Result, GeneralError>
}