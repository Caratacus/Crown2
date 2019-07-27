package org.crown.project.monitor.exceLog.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.crown.common.annotation.Log;
import org.crown.common.enums.BusinessType;
import org.crown.common.utils.StringUtils;
import org.crown.common.utils.poi.ExcelUtils;
import org.crown.framework.model.ExcelDTO;
import org.crown.framework.responses.ApiResponses;
import org.crown.framework.web.controller.WebController;
import org.crown.framework.web.page.TableData;
import org.crown.project.monitor.exceLog.domain.ExceLog;
import org.crown.project.monitor.exceLog.service.IExceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;

/**
 * 异常日志信息操作处理
 *
 * @author Caratacus
 */
@Controller
@RequestMapping("/monitor/exceLog")
public class ExceLogController extends WebController<ExceLog> {

	private final String prefix = "monitor/exceLog";

	@Autowired
	private IExceLogService exceLogService;

	@RequiresPermissions("monitor:exceLog:view")
	@GetMapping
	public String exceLog() {
		return prefix + "/exceLog";
	}

	/**
     * 查询异常日志列表
     */
	@RequiresPermissions("monitor:exceLog:list")
	@PostMapping("/list")
	@ResponseBody
	public ApiResponses<TableData<ExceLog>> list(ExceLog exceLog) {
		startPage();
		List<ExceLog> list = exceLogService.list(Wrappers.<ExceLog>lambdaQuery(exceLog));
		return success(getTableData(list));
	}

	/**
     * 导出异常日志列表
     */
	@RequiresPermissions("monitor:exceLog:export")
	@PostMapping("/export")
	@ResponseBody
	public ApiResponses<ExcelDTO> export(ExceLog exceLog) {
		List<ExceLog> list = exceLogService.list(Wrappers.<ExceLog>lambdaQuery(exceLog));
		ExcelUtils<ExceLog> util = new ExcelUtils<>(ExceLog.class);
		return success(new ExcelDTO(util.exportExcel(list, "exceLog")));
	}

	/**
     * 新增异常日志
     */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
     * 新增保存异常日志
     */
	@RequiresPermissions("monitor:exceLog:add")
	@Log(title = "异常日志", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public ApiResponses<Void> addSave(@Validated ExceLog exceLog) {
		exceLogService.save(exceLog);
		return success();
	}

	/**
     * 修改异常日志
     */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		ExceLog exceLog = exceLogService.getById(id);
		mmap.put("exceLog", exceLog);
		return prefix + "/edit";
	}

	/**
     * 修改保存异常日志
     */
	@RequiresPermissions("monitor:exceLog:edit")
	@Log(title = "异常日志", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public ApiResponses<Void> editSave(@Validated ExceLog exceLog) {
		exceLogService.updateById(exceLog);
		return success();
	}

	/**
     * 删除异常日志
     */
	@RequiresPermissions("monitor:exceLog:remove")
	@Log(title = "异常日志", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public ApiResponses<Void> remove(String ids) {
		exceLogService.remove(Wrappers.<ExceLog>lambdaQuery().in(ExceLog::getId, StringUtils.split2List(ids)));
		return success();
	}

}
