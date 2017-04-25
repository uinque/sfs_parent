package com.hk.sfs.controller;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.hk.commons.utils.time.FormatConstants;
import com.hk.sfs.filter.RequestContext;
import com.hk.sfs.utils.DatesEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public abstract class RestController<T> {
	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/** The entity class. */
	protected Class<T> entityClass;

	/**
	 * Instantiates a new abstract base rest controller.
	 */
	public RestController() {
		super();
		try {
			TypeToken<?> genericTypeToken = TypeToken.of(getClass());
			this.entityClass = (Class<T>) genericTypeToken.resolveType(RestController.class.getTypeParameters()[0])
					.getRawType();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Instantiates a new abstract base rest controller.
	 * 
	 * @param entityClass
	 *            the entity class
	 */
	public RestController(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
		binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
        binder.registerCustomEditor(Date.class, new DatesEditor(FormatConstants.DATE_TIME_FORMAT, true));
		toInitBinder(binder);
		List<String> disallowedFiled = Lists.newArrayList();
		disallowedFiled.add("id");
		toDisallowedFiled(disallowedFiled);
		binder.setDisallowedFields(disallowedFiled.toArray(new String[disallowedFiled.size()]));
	}

	protected void toInitBinder(WebDataBinder binder) {

	}

	protected void toDisallowedFiled(List<String> disallowedFiled) {

	}

	/**
	 * Inits the.
	 * 
	 * @param model
	 *            the model
	 */
	@ModelAttribute
	public void init(Model model) {
		model.addAttribute("now", new java.sql.Timestamp(System.currentTimeMillis()));
	}

	/**
	 * Gets the model.
	 * 
	 * @param id
	 *            the id
	 * @return the model
	 */
	@ModelAttribute
	public T getModel(@RequestParam(required = false) Long id) {
		T entity = null;
		if (id != null) {
			entity = toModel(id);
		}
		if (entity == null) {
			try {
				entity = entityClass.newInstance();
			} catch (Exception e) {
			}
		}
		return entity;
	}

	/**
	 * To model.
	 * 
	 * @param id
	 *            the id
	 * @return the t
	 */
	protected T toModel(Long id) {
		return null;
	}

	/**
	 * Index.
	 * 
	 * @param model
	 *            the model
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the string
	 */
	@RequestMapping
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * _new.
	 * 
	 * @param model
	 *            the model
	 * @param entity
	 *            the entity
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the string
	 */
	@RequestMapping(value = "/new")
	public String _new(Model model, T entity, HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Show.
	 * 
	 * @param model
	 *            the model
	 * @param id
	 *            the id
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the string
	 */
	@RequestMapping(value = "/{id}")
	public String show(Model model, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Edits the.
	 * 
	 * @param model
	 *            the model
	 * @param id
	 *            the id
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the string
	 */
	@RequestMapping(value = "/{id}/edit")
	public String edit(Model model, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Creates the.
	 * 
	 * @param model
	 *            the model
	 * @param entity
	 *            the entity
	 * @param errors
	 *            the errors
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the string
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String create(Model model, RedirectAttributes redirectAttributes, @Validated T entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Update.
	 * 
	 * @param model
	 *            the model
	 * @param id
	 *            the id
	 * @param entity
	 *            the entity
	 * @param errors
	 *            the errors
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the string
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public String update(RedirectAttributes model, @PathVariable Long id, @Validated T entity, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Delete.
	 * 
	 * @param model
	 *            the model
	 * @param id
	 *            the id
	 * @param entity
	 *            the entity
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the string
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(RedirectAttributes model, @PathVariable Long id, T entity, HttpServletRequest request,
			HttpServletResponse response) {
		throw new UnsupportedOperationException("not yet implement");
	}

	/**
	 * Batch delete.
	 *
	 * @param model
	 *            the model
	 * @param items
	 *            the items
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the string
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public String batchDelete(RedirectAttributes model, @RequestParam("items[]") Long[] items,
			HttpServletRequest request, HttpServletResponse response) {
		throw new UnsupportedOperationException("not yet implement");
	}

	protected ServletContext servletContext() {
		return RequestContext.get().context();
	}

	/**
	 * Gets the servlet context.
	 *
	 * @return the servlet context
	 */
	protected ServletContext getServletContext() {
		return servletContext();
	}

	protected HttpServletRequest request() {
		return RequestContext.get().request();
	}

	protected HttpServletRequest getRequest() {
		return request();
	}

	protected HttpServletResponse response() {
		return RequestContext.get().response();
	}

	protected HttpServletResponse getResponse() {
		return response();
	}

	protected HttpSession session() {
		return request().getSession();
	}

	@SuppressWarnings("unchecked")
	public Map<String, String[]> getAsMap() {
		return ImmutableMap.copyOf(request().getParameterMap());
	}

	public String getAsString(String setting) {
		String[] o = get(setting);
		return ((o != null) && (o.length >= 1)) ? o[0] : null;
	}

	public String getAsString(String setting, String defaultValue) {
		String retVal = getAsString(setting);
		return retVal == null ? defaultValue : retVal;
	}

	public String[] get(String setting) {
		String[] retVal = bulidRequestParams().get(setting);
		if (retVal != null) {
			return retVal;
		}
		return bulidRequestParams().get(setting.toLowerCase());
	}

	public String[] get(String setting, String[] defaultValue) {
		String[] retVal = bulidRequestParams().get(setting);
		return retVal == null ? defaultValue : retVal;
	}

	public Float getAsFloat(String setting, Float defaultValue) {
		String sValue = getAsString(setting);
		if (sValue == null) {
			return defaultValue;
		}
		try {
			return Float.parseFloat(sValue);
		} catch (NumberFormatException e) {
			throw Throwables.propagate(e);
		}
	}

	public Double getAsDouble(String setting, Double defaultValue) {
		String sValue = getAsString(setting);
		if (sValue == null) {
			return defaultValue;
		}
		try {
			return Double.parseDouble(sValue);
		} catch (NumberFormatException e) {
			throw Throwables.propagate(e);
		}
	}

	public Integer getAsInt(String setting, Integer defaultValue) {
		String sValue = getAsString(setting);
		if (sValue == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(sValue);
		} catch (NumberFormatException e) {
			throw Throwables.propagate(e);
		}
	}

	public Long getAsLong(String setting, Long defaultValue) {
		String sValue = getAsString(setting);
		if (sValue == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(sValue);
		} catch (NumberFormatException e) {
			throw Throwables.propagate(e);
		}
	}

	public Boolean getAsBoolean(String setting, Boolean defaultValue) {
		String value = getAsString(setting, defaultValue.toString());
		return Boolean.valueOf(value);
	}

	public static String getServerPath(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 ? "" : (":" + request.getServerPort()))
				+ ("/".equalsIgnoreCase(request.getContextPath()) ? "" : request.getContextPath());
	}

	protected ImmutableMap<String, String[]> bulidRequestParams() {
		return ImmutableMap.copyOf(getParametersStartingWith(getRequest(), null));

	}

	protected Map<String, String[]> getParametersStartingWith(ServletRequest request, String prefix) {
		Assert.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, String[]> params = new TreeMap<String, String[]>();
		if (prefix == null) {
			prefix = "";
		}
		while ((paramNames != null) && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if ((values == null) || (values.length == 0)) {
					// Do nothing, no values found at all.
				} else {
					params.put(unprefixed, values);
				}
			}
		}
		return params;
	}

}
