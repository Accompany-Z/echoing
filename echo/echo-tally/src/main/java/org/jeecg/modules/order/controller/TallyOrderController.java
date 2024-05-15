package org.jeecg.modules.order.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.order.entity.TallyOrder;
import org.jeecg.modules.order.service.ITallyOrderService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 订单信息
 * @Author: jeecg-boot
 * @Date:   2024-05-15
 * @Version: V1.0
 */
@Api(tags="订单信息")
@RestController
@RequestMapping("/order")
@Slf4j
public class TallyOrderController extends JeecgController<TallyOrder, ITallyOrderService> {
	@Autowired
	private ITallyOrderService tallyOrderService;
	
	/**
	 * 分页列表查询
	 *
	 * @param tallyOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "订单信息-分页列表查询")
	@ApiOperation(value="订单信息-分页列表查询", notes="订单信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TallyOrder>> queryPageList(TallyOrder tallyOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TallyOrder> queryWrapper = QueryGenerator.initQueryWrapper(tallyOrder, req.getParameterMap());
		Page<TallyOrder> page = new Page<TallyOrder>(pageNo, pageSize);
		IPage<TallyOrder> pageList = tallyOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param tallyOrder
	 * @return
	 */
	@AutoLog(value = "订单信息-添加")
	@ApiOperation(value="订单信息-添加", notes="订单信息-添加")
	//@RequiresPermissions("org.jeecg.modules:tally_order:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TallyOrder tallyOrder) {
		tallyOrderService.save(tallyOrder);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param tallyOrder
	 * @return
	 */
	@AutoLog(value = "订单信息-编辑")
	@ApiOperation(value="订单信息-编辑", notes="订单信息-编辑")
	//@RequiresPermissions("org.jeecg.modules:tally_order:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TallyOrder tallyOrder) {
		tallyOrderService.updateById(tallyOrder);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "订单信息-通过id删除")
	@ApiOperation(value="订单信息-通过id删除", notes="订单信息-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:tally_order:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		tallyOrderService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "订单信息-批量删除")
	@ApiOperation(value="订单信息-批量删除", notes="订单信息-批量删除")
	//@RequiresPermissions("org.jeecg.modules:tally_order:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tallyOrderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "订单信息-通过id查询")
	@ApiOperation(value="订单信息-通过id查询", notes="订单信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TallyOrder> queryById(@RequestParam(name="id",required=true) String id) {
		TallyOrder tallyOrder = tallyOrderService.getById(id);
		if(tallyOrder==null) {
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
        return super.exportXls(request, tallyOrder, TallyOrder.class, "订单信息");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("tally_order:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TallyOrder.class);
    }

}
