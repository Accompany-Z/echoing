package org.jeecg.modules.flow.service.impl;

import org.jeecg.modules.flow.entity.TallyFlow;
import org.jeecg.modules.flow.mapper.TallyFlowMapper;
import org.jeecg.modules.flow.service.TallyFlowService;
import org.jeecg.modules.order.entity.TallyOrder;
import org.jeecg.modules.order.service.TallyOrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 订单流水
 * @Author: jeecg-boot
 * @Date: 2024-08-08
 * @Version: V1.0
 */
@Service
public class TallyFlowServiceImpl extends ServiceImpl<TallyFlowMapper, TallyFlow> implements TallyFlowService {

    @Resource
    private TallyOrderService orderService;

    /**
     * 保存流水并修改订单余量
     *
     * @author zwf
     * @date created in 2024/8/19 13:48
     * @param: tallyFlow
     */
    @Override
    public void saveFlowOrder(TallyFlow tallyFlow) {
        TallyOrder order = orderService.getById(tallyFlow.getOrderId());

        BigDecimal orderMargin = order.getOrderMargin();
        Double weight = tallyFlow.getWeight();
        // 将 Double 转换为 BigDecimal
        BigDecimal weightBigDecimal = BigDecimal.valueOf(weight);
        // 进行减法操作
        BigDecimal result = orderMargin.subtract(weightBigDecimal);

        if (result.compareTo(BigDecimal.ZERO) == 0) {
            order.setFinishTime(new Date());
        }
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            // TODO 不可小于0
        }

        order.setOrderMargin(result);

        orderService.updateById(order);

        this.save(tallyFlow);
    }
}
