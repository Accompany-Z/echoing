package org.jeecg.modules.order.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.order.entity.TallyOrder;
import org.jeecg.modules.order.service.TallyOrderService;

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
 * @Description: 订单
 * @Author: jeecg-boot
 * @Date: 2024-08-12
 * @Version: V1.0
 */
@Api(tags = "订单")
@RestController
@RequestMapping("/order")
@Slf4j
public class TallyOrderController extends JeecgController<TallyOrder, TallyOrderService> {
    @Autowired
    private TallyOrderService tallyOrderService;

    /**
     * 分页列表查询
     *
     * @param tallyOrder
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "订单-分页列表查询")
    @ApiOperation(value = "订单-分页列表查询", notes = "订单-分页列表查询")
    @GetMapping(value = "/rootList")
    public Result<IPage<TallyOrder>> queryPageList(TallyOrder tallyOrder,
                                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   HttpServletRequest req) {
        String hasQuery = req.getParameter("hasQuery");
        if (hasQuery != null && "true".equals(hasQuery)) {
            QueryWrapper<TallyOrder> queryWrapper = QueryGenerator.initQueryWrapper(tallyOrder, req.getParameterMap());
            List<TallyOrder> list = tallyOrderService.queryTreeListNoPage(queryWrapper);
            IPage<TallyOrder> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } else {
            String parentId = tallyOrder.getPid();
            if (oConvertUtils.isEmpty(parentId)) {
                parentId = "0";
            }
            tallyOrder.setPid(null);
            QueryWrapper<TallyOrder> queryWrapper = QueryGenerator.initQueryWrapper(tallyOrder, req.getParameterMap());
            // 使用 eq 防止模糊查询
            queryWrapper.eq("pid", parentId);
            Page<TallyOrder> page = new Page<TallyOrder>(pageNo, pageSize);
            IPage<TallyOrder> pageList = tallyOrderService.page(page, queryWrapper);
            return Result.OK(pageList);
        }
    }

    /**
     * 【vue3专用】加载节点的子数据
     *
     * @param pid
     * @return
     */
    @RequestMapping(value = "/loadTreeChildren", method = RequestMethod.GET)
    public Result<List<SelectTreeModel>> loadTreeChildren(@RequestParam(name = "pid") String pid) {
        Result<List<SelectTreeModel>> result = new Result<>();
        try {
            List<SelectTreeModel> ls = tallyOrderService.queryListByPid(pid);
            result.setResult(ls);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 【vue3专用】加载一级节点/如果是同步 则所有数据
     *
     * @param async
     * @param pcode
     * @return
     */
    @RequestMapping(value = "/loadTreeRoot", method = RequestMethod.GET)
    public Result<List<SelectTreeModel>> loadTreeRoot(@RequestParam(name = "async") Boolean async, @RequestParam(name = "pcode") String pcode) {
        Result<List<SelectTreeModel>> result = new Result<>();
        try {
            List<SelectTreeModel> ls = tallyOrderService.queryListByCode(pcode);
            if (!async) {
                loadAllChildren(ls);
            }
            result.setResult(ls);
            result.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 【vue3专用】递归求子节点 同步加载用到
     *
     * @param ls
     */
    private void loadAllChildren(List<SelectTreeModel> ls) {
        for (SelectTreeModel tsm : ls) {
            List<SelectTreeModel> temp = tallyOrderService.queryListByPid(tsm.getKey());
            if (temp != null && temp.size() > 0) {
                tsm.setChildren(temp);
                loadAllChildren(temp);
            }
        }
    }

    /**
     * 获取子数据
     *
     * @param tallyOrder
     * @param req
     * @return
     */
    //@AutoLog(value = "订单-获取子数据")
    @ApiOperation(value = "订单-获取子数据", notes = "订单-获取子数据")
    @GetMapping(value = "/childList")
    public Result<IPage<TallyOrder>> queryPageList(TallyOrder tallyOrder, HttpServletRequest req) {
        QueryWrapper<TallyOrder> queryWrapper = QueryGenerator.initQueryWrapper(tallyOrder, req.getParameterMap());
        List<TallyOrder> list = tallyOrderService.list(queryWrapper);
        IPage<TallyOrder> pageList = new Page<>(1, 10, list.size());
        pageList.setRecords(list);
        return Result.OK(pageList);
    }

    /**
     * 批量查询子节点
     *
     * @param parentIds 父ID（多个采用半角逗号分割）
     * @param parentIds
     * @return 返回 IPage
     * @return
     */
    //@AutoLog(value = "订单-批量获取子数据")
    @ApiOperation(value = "订单-批量获取子数据", notes = "订单-批量获取子数据")
    @GetMapping("/getChildListBatch")
    public Result getChildListBatch(@RequestParam("parentIds") String parentIds) {
        try {
            QueryWrapper<TallyOrder> queryWrapper = new QueryWrapper<>();
            List<String> parentIdList = Arrays.asList(parentIds.split(","));
            queryWrapper.in("pid", parentIdList);
            List<TallyOrder> list = tallyOrderService.list(queryWrapper);
            IPage<TallyOrder> pageList = new Page<>(1, 10, list.size());
            pageList.setRecords(list);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("批量查询子节点失败：" + e.getMessage());
        }
    }

    /**
     * 添加
     *
     * @param tallyOrder
     * @return
     */
    @AutoLog(value = "订单-添加")
    @ApiOperation(value = "订单-添加", notes = "订单-添加")
    //@RequiresPermissions("org.jeecg.modules:tally_order:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody TallyOrder tallyOrder) {
        tallyOrderService.addTallyOrder(tallyOrder);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param tallyOrder
     * @return
     */
    @AutoLog(value = "订单-编辑")
    @ApiOperation(value = "订单-编辑", notes = "订单-编辑")
    //@RequiresPermissions("org.jeecg.modules:tally_order:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody TallyOrder tallyOrder) {
        tallyOrderService.updateTallyOrder(tallyOrder);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "订单-通过id删除")
    @ApiOperation(value = "订单-通过id删除", notes = "订单-通过id删除")
    //@RequiresPermissions("org.jeecg.modules:tally_order:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        tallyOrderService.deleteTallyOrder(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "订单-批量删除")
    @ApiOperation(value = "订单-批量删除", notes = "订单-批量删除")
    //@RequiresPermissions("org.jeecg.modules:tally_order:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.tallyOrderService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "订单-通过id查询")
    @ApiOperation(value = "订单-通过id查询", notes = "订单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<TallyOrder> queryById(@RequestParam(name = "id", required = true) String id) {
        TallyOrder tallyOrder = tallyOrderService.getById(id);
        if (tallyOrder == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(tallyOrder);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param tallyOrder
     */
    //@RequiresPermissions("org.jeecg.modules:tally_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TallyOrder tallyOrder) {
        return super.exportXls(request, tallyOrder, TallyOrder.class, "订单");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresPermissions("org.jeecg.modules:tally_order:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TallyOrder.class);
    }

}
