package com.hk.sfs.controller;

import com.google.common.base.Throwables;
import com.hk.sfs.metadata.dao.GeneralJpaRepository;
import com.hk.sfs.metadata.entity.BaseEntity;
import com.hk.sfs.utils.AjaxUtils;
import com.hk.sfs.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 抽象的控制器基类
 * Created by wuwei on 2016/1/25.
 */
public abstract class BaseController<T extends BaseEntity> extends RestController<T> {

	@Resource
	protected GeneralJpaRepository generalJpaRepository;

	@Override
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		String msg = toIndex(model, request, response, requestMapping() + "/index");
		return msg;
	}

	protected String toIndex(Model model, HttpServletRequest request, HttpServletResponse response, String defaultView) {
		return defaultView;
	}

	@Override
	public String _new(Model model, T entity, HttpServletRequest request, HttpServletResponse response) {
		String msg = toNew(model, entity, request, response, requestMapping() + "/new");
		return msg;
	}

	protected String toNew(Model model, T entity, HttpServletRequest request, HttpServletResponse response,
			String defaultView) {
		model.addAttribute("entity", entity);
		return defaultView;
	}

	@Override
	public String show(Model model, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		String msg = toShow(model, id, request, response, requestMapping() + "/show");
		return msg;
	}

	protected String toShow(Model model, Long id, HttpServletRequest request, HttpServletResponse response,
			String defaultView) {
		model.addAttribute("entity", toModel(id));
		return defaultView;
	}

	@Override
	public String edit(Model model, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		String msg = toEdit(model, id, request, response, requestMapping() + "/show");
		return msg;
	}

	protected String toEdit(Model model, Long id, HttpServletRequest request, HttpServletResponse response,
			String defaultView) {
		model.addAttribute("entity", toModel(id));
		return defaultView;
	}

	@Override
	public String create(Model model, RedirectAttributes redirectAttributes, @Validated T entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) {
		if (errors.hasErrors()) {
			return requestMapping() + "/new";
		}
		String msg = "redirect:" + requestMapping();
		try {
			msg = toCreate(model, redirectAttributes, entity, errors, request, response, msg);
			redirectAttributes.addFlashAttribute(Constants.STATUS_CODE, AjaxUtils.STATUS_CODE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute(Constants.STATUS_CODE, AjaxUtils.STATUS_CODE_FAILURE);
			throw Throwables.propagate(e);
		}
		return msg;
	}

	protected String toCreate(Model model, RedirectAttributes redirectAttributes, T entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response, String defaultView) {
		generalJpaRepository.save(entity);
		return defaultView;
	}

	@Override
	public String update(RedirectAttributes model, @PathVariable Long id, @Validated T entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) {
		if (errors.hasErrors()) {
			return requestMapping() + "/edit";
		}
		String msg = "redirect:" + requestMapping();
		try {
			msg = toUpdate(model, id, entity, errors, request, response, msg);
			model.addFlashAttribute(Constants.STATUS_CODE, AjaxUtils.STATUS_CODE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			model.addFlashAttribute(Constants.STATUS_CODE, AjaxUtils.STATUS_CODE_FAILURE);
			model.addFlashAttribute(Constants.ERROR_MSG, Throwables.getRootCause(e));
			return "redirect:" + requestMapping() + "/" + id + "/edit";
		}
		return msg;
	}

	protected String toUpdate(RedirectAttributes model, Long id, T entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response, String defaultView) {
		generalJpaRepository.save(entity);
		return defaultView;
	}

	@Override
	public String delete(RedirectAttributes model, @PathVariable Long id, T entity, HttpServletRequest request,
			HttpServletResponse response) {
		String msg = "redirect:" + requestMapping();
		try {
			msg = toDelete(model, id, entity, request, response, msg);
			model.addFlashAttribute(Constants.STATUS_CODE, AjaxUtils.STATUS_CODE_SUCCESS);
		} catch (Exception e2) {
			e2.printStackTrace();
			model.addFlashAttribute(Constants.STATUS_CODE, AjaxUtils.STATUS_CODE_FAILURE);
			throw Throwables.propagate(e2);
		}
		return msg;
	}

	protected String toDelete(RedirectAttributes model, Long id, T entity, HttpServletRequest request,
			HttpServletResponse response, String defaultView) {
		if (null != entity && entity.getId() != null) {
			generalJpaRepository.delete(id, entityClass);
		}
		return defaultView;
	}

	@Override
	public String batchDelete(RedirectAttributes model, @RequestParam("items[]") Long[] items,
			HttpServletRequest request, HttpServletResponse response) {
		String msg = "redirect:" + requestMapping();
		try {
			msg = toBatchDelete(model, items, request, response, msg);
			model.addFlashAttribute(Constants.STATUS_CODE, AjaxUtils.STATUS_CODE_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			model.addFlashAttribute(Constants.STATUS_CODE, AjaxUtils.STATUS_CODE_FAILURE);
			throw Throwables.propagate(e);

		}
		return msg;
	}

	protected String toBatchDelete(RedirectAttributes model, Long[] items, HttpServletRequest request,
			HttpServletResponse response, String defaultView) {
		if (null != items && items.length > 0) {
			for (Long id : items) {
				generalJpaRepository.delete(id, entityClass);
			}
		}
		return null;
	}

	public String requestMapping() {
		RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
		if (requestMapping != null) {
			if (requestMapping.value().length <= 0) {
				return "";
			}
			return requestMapping.value()[0];
		}
		return "";
	}

	@Override
	protected T toModel(Long id) {
		return (T) generalJpaRepository.findOne(id, entityClass);
	}

	/**
	 * 创建分页
	 * @param pageNo  当前页
	 * @return
	 */
	public Pageable buildPageable(Integer pageNo) {
		return buildPageable(pageNo, null, null, null);
	}

	/**
	 * 创建分页
	 * @param pageNo  当前页
	 * @param pageSize  每页小大
	 * @return
	 */
	public Pageable buildPageable(Integer pageNo, Integer pageSize) {
		return buildPageable(pageNo, pageSize, null, null);
	}

	/**
	 * 创建分页
	 * @param pageNo  当前页
	 * @param pageSize  每页小大
	 * @param orderBy   排序字段
	 * @param sort      降序/升序
	 * @return
	 */
	public Pageable buildPageable(Integer pageNo, Integer pageSize, String orderBy, Direction sort) {
		int defaultPageSize = 20;
		int defaultPageNo = 1;
		Direction defaultSort = Direction.DESC;

		if (null == pageNo || pageNo.intValue() <= 0) {
			pageNo = defaultPageNo;
		}
		if (null == pageSize || pageNo.intValue() <= 0) {
			pageSize = defaultPageSize;
		}
		if (sort != null) {
			defaultSort = sort;
		}

		if (StringUtils.isBlank(orderBy)) {
			return new PageRequest(pageNo - 1, pageSize);
		}
		return new PageRequest(pageNo - 1, pageSize, new Sort(defaultSort, orderBy));
	}
}
