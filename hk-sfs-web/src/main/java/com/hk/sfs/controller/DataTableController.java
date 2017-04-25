package com.hk.sfs.controller;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.hk.commons.web.utils.ResponseUtils;
import com.hk.sfs.utils.*;
import com.hk.sfs.searcher.DynamicSpecifications;
import com.hk.sfs.searcher.WebSearchFilter;
import com.hk.sfs.metadata.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 拥有grid的控制器支持ajax
 */
public class DataTableController<T extends BaseEntity> extends BaseController<T> {
	@Override
	public String create(final Model model, final RedirectAttributes redirectAttributes, @Validated final T entity,
			final BindingResult errors, final HttpServletRequest request, final HttpServletResponse response) {
		if (AjaxUtils.isAjaxRequest(request)) {
			JsonActionTemplate.renderJson(new JsonActionTemplate.SingleObjectMeesageCallBack(response) {
				@Override
				protected Object toObject() {
					try {
						DataTableController.super.create(model, redirectAttributes, entity, errors, request,
								response);
						return AjaxUtils.newOk().toString();
					} catch (Exception e) {
						e.printStackTrace();
						return AjaxUtils.newError(Throwables.getStackTraceAsString(e));
					}
				}

				@Override
				public String toWriter(Object object) {
                    if (object instanceof AjaxUtils){
						ResponseUtils.renderJson(response, object.toString());
                    }else {
						ResponseUtils.renderJson(response, (String) object);
                    }
					return null;
				}
			});
		} else {
			return super.create(model, redirectAttributes, entity, errors, request, response);
		}
		return null;
	}

	@Override
	public String update(final RedirectAttributes model, @PathVariable final Long id, @Validated final T entity,
			final BindingResult errors, final HttpServletRequest request, final HttpServletResponse response) {
		if (AjaxUtils.isAjaxRequest(request)) {
			JsonActionTemplate.renderJson(new JsonActionTemplate.SingleObjectMeesageCallBack(response) {
				@Override
				protected Object toObject() {
					try {
						DataTableController.super.update(model, id, entity, errors, request, response);
						return AjaxUtils.newOk().toString();
					} catch (Exception e) {
						e.printStackTrace();
						return AjaxUtils.newError(Throwables.getStackTraceAsString(e));
					}
				}

				@Override
				public String toWriter(Object object) {
                    if (object instanceof AjaxUtils){
						ResponseUtils.renderJson(response, object.toString());
                    }else {
						ResponseUtils.renderJson(response, (String) object);
                    }
					ResponseUtils.renderJson(response, (String) object);
					return null;
				}
			});
		} else {
			return super.update(model, id, entity, errors, request, response);
		}
		return null;
	}

	@Override
	public String delete(final RedirectAttributes model, @PathVariable final Long id, final T entity,
			final HttpServletRequest request, final HttpServletResponse response) {
		if (AjaxUtils.isAjaxRequest(request)) {
			JsonActionTemplate.renderJson(new JsonActionTemplate.SingleObjectMeesageCallBack(response) {
				@Override
				protected Object toObject() {
					try {
						return DataTableController.super.delete(model, id, entity, request, response);
					} catch (Exception e) {
						e.printStackTrace();
						return AjaxUtils.newError(Throwables.getStackTraceAsString(e));
					}
				}

				@Override
				public String toWriter(Object object) {
					ResponseUtils.renderJson(response, (String) object);
					return null;
				}
			});
		} else {
			return super.delete(model, id, entity, request, response);
		}
		return null;
	}

	@Override
	public String batchDelete(final RedirectAttributes model, @RequestParam("items[]") final Long[] items,
			final HttpServletRequest request, final HttpServletResponse response) {
		if (AjaxUtils.isAjaxRequest(request)) {
			JsonActionTemplate.renderJson(new JsonActionTemplate.SingleObjectMeesageCallBack(response) {
				@Override
				protected Object toObject() {
					return DataTableController.super.batchDelete(model, items, request, response);
				}

				@Override
				public String toWriter(Object object) {
					ResponseUtils.renderJson(response, (String) object);
					return null;
				}
			});
		} else {
			return super.batchDelete(model, items, request, response);
		}
		return null;
	}

	@RequestMapping(value = "/dataTable", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody
	String dataTable(Model model, DataTable dataTable, HttpServletRequest request, HttpServletResponse response) {
		String msg = AjaxUtils.newOk().string();
		try {
			msg = toDataTable(model, dataTable, request, response, msg);
		} catch (Exception e) {
			e.printStackTrace();
			msg = AjaxUtils.newError(Throwables.getRootCause(e).toString()).string();
		}
		return msg;
	}

	protected String toDataTable(Model model, final DataTable dataTable, HttpServletRequest request,
			HttpServletResponse response, String msg) {
		Page page = generalJpaRepository.findAll(filter(model, request), toPageRequest(dataTable), entityClass);
		return dataTable.toJson(page, new DataTable.ParamCallbak<DataTable.PageJsonInfo>() {
			@Override
			public void call(DataTable.PageJsonInfo pageJsonInfo, JsonMapper jsonMapper) {
				toParamCallbak(pageJsonInfo, dataTable);
			}

			@Override
			public String after(String json, JsonMapper jsonMapper) {
				return json;
			}

		});
	}

	protected void toParamCallbak(DataTable.PageJsonInfo pageJsonInfo, DataTable dataTable) {
	}

	protected Specification<T> filter(Model model, HttpServletRequest request) {
		return DynamicSpecifications.bySearchFilter(request, entityClass, toFilter(request));
	}

	protected List<WebSearchFilter> toFilter(HttpServletRequest request) {
		return Lists.newArrayList();
	}

	protected Pageable toPageRequest(DataTable dataTable) {
		PageRequest pageRequest = dataTable.toPageRequest();
		return pageRequest;
	}
}
