package org.jeecg.modules.flow.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.flow.entity.TallyFlow;
import org.jeecg.modules.flow.service.TallyFlowService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 订单流水
 * @Author: jeecg-boot
 * @Date: 2024-08-08
 * @Version: V1.0
 */
@Api(tags = "订单流水")
@RestController
@RequestMapping("/flow")
@Slf4j
public class TallyFlowController extends JeecgController<TallyFlow, TallyFlowService> {
    @Autowired
    private TallyFlowService tallyFlowService;

    /**
     * 分页列表查询
     *
     * @param tallyFlow
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "订单流水-分页列表查询")
    @ApiOperation(value = "订单流水-分页列表查询", notes = "订单流水-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<TallyFlow>> queryPageList(TallyFlow tallyFlow,
                                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                  HttpServletRequest req) {
        QueryWrapper<TallyFlow> queryWrapper = QueryGenerator.initQueryWrapper(tallyFlow, req.getParameterMap());
        Page<TallyFlow> page = new Page<TallyFlow>(pageNo, pageSize);
        IPage<TallyFlow> pageList = tallyFlowService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param tallyFlow
     * @return
     */
    @AutoLog(value = "订单流水-添加")
    @ApiOperation(value = "订单流水-添加", notes = "订单流水-添加")
    //@RequiresPermissions("org.jeecg.modules:tally_flow:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody TallyFlow tallyFlow) {
        tallyFlowService.saveFlowOrder(tallyFlow);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param tallyFlow
     * @return
     */
    @AutoLog(value = "订单流水-编辑")
    @ApiOperation(value = "订单流水-编辑", notes = "订单流水-编辑")
    //@RequiresPermissions("org.jeecg.modules:tally_flow:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody TallyFlow tallyFlow) {
        tallyFlowService.updateById(tallyFlow);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单流水-通过id删除")
    @ApiOperation(value = "订单流水-通过id删除", notes = "订单流水-通过id删除")
    //@RequiresPermissions("org.jeecg.modules:tally_flow:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        tallyFlowService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "订单流水-批量删除")
    @ApiOperation(value = "订单流水-批量删除", notes = "订单流水-批量删除")
    //@RequiresPermissions("org.jeecg.modules:tally_flow:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.tallyFlowService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "订单流水-通过id查询")
    @ApiOperation(value = "订单流水-通过id查询", notes = "订单流水-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<TallyFlow> queryById(@RequestParam(name = "id", required = true) String id) {
        TallyFlow tallyFlow = tallyFlowService.getById(id);
        if (tallyFlow == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(tallyFlow);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param tallyFlow
     */
    //@RequiresPermissions("org.jeecg.modules:tally_flow:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TallyFlow tallyFlow) {
        return super.exportXls(request, tallyFlow, TallyFlow.class, "订单流水");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("tally_flow:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TallyFlow.class);
    }

}
