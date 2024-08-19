package org.jeecg.modules.flow.service;

import org.jeecg.modules.flow.entity.TallyFlow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 订单流水
 * @Author: jeecg-boot
 * @Date:   2024-08-08
 * @Version: V1.0
 */
public interface TallyFlowService extends IService<TallyFlow> {

    /**
     * 保存流水并修改订单余量
     *
     * @author zwf
     * @date created in 2024/8/19 13:47
     * @param: tallyFlow
     */
    void saveFlowOrder(TallyFlow tallyFlow);
}
