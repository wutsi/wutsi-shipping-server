package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.shipping.dao.RateRepository
import com.wutsi.ecommerce.shipping.service.SecurityManager
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class DeleteRateDelegate(
    private val dao: RateRepository,
    private val securityManager: SecurityManager
) {
    @Transactional
    public fun invoke(id: Long) {
        val rate = dao.findById(id)

        if (rate.isPresent) {
            securityManager.checkOwnership(rate.get())
            dao.delete(rate.get())
        }
    }
}
