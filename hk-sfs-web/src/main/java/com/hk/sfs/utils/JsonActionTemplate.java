package com.hk.sfs.utils;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public final class JsonActionTemplate {

	/** The logger. */
	private static Logger logger = LoggerFactory
			.getLogger(JsonActionTemplate.class);

	public static interface MeesageCallback {

		/**
		 * Business.
		 *
		 * @param returnMsg
		 *            the return msg
		 */
		void business(Map<String, Object> returnMsg);

		/**
		 * To writer.
		 *
		 * @param object
		 *            the object
		 * @return the string
		 */
		String toWriter(Object object);
	}

	/**
	 * The Class DefaultMeesageCallback.
	 * 
	 * @author l.xue.nong
	 */
	public static abstract class AbstractMeesageCallback implements
			MeesageCallback {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * cn.com.rebirth.knowledge.web.admin.JsonActionTemplate.MeesageCallback
		 * #toWriter(java.lang.Object)
		 */
		@Override
		public String toWriter(Object object) {
			return JsonMapper.nonDefaultMapper().toJson(object);
		}

	}

	/**
	 * The Class AbstractResponseMeesageCallback.
	 * 
	 * @author l.xue.nong
	 */
	public static abstract class AbstractResponseMeesageCallback extends
			AbstractMeesageCallback implements MeesageCallback {

		/** The response. */
		private final HttpServletResponse response;

		/**
		 * Instantiates a new abstract response meesage callback.
		 * 
		 * @param response
		 *            the response
		 */
		public AbstractResponseMeesageCallback(HttpServletResponse response) {
			super();
			this.response = response;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see cn.com.rebirth.knowledge.web.admin.JsonActionTemplate.
		 * AbstractMeesageCallback#toWriter(java.lang.Object)
		 */
		@Override
		public String toWriter(Object object) {
			ResponseTypeOutputUtils.renderJson(response, object);
			return super.toWriter(object);
		}

	}

	/**
	 * The Class SingleObjectMeesageCallBack.
	 * 
	 * @author l.xue.nong
	 */
	public static abstract class SingleObjectMeesageCallBack extends
			AbstractResponseMeesageCallback implements MeesageCallback {

		/**
		 * Instantiates a new single object meesage call back.
		 * 
		 * @param response
		 *            the response
		 */
		public SingleObjectMeesageCallBack(HttpServletResponse response) {
			super(response);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * cn.com.rebirth.knowledge.web.admin.JsonActionTemplate.MeesageCallback
		 * #business(java.util.Map)
		 */
		@Override
		public void business(Map<String, Object> returnMsg) {
			Object o = toObject();
			returnMsg.put(SingleObjectMeesageCallBack.class.getName(), o);
		}

		/**
		 * To object.
		 *
		 * @return the object
		 */
		protected abstract Object toObject();
	}

	/**
	 * Render json.
	 * 
	 * @param meesageCallback
	 *            the meesage callback
	 * @return the string
	 */
	public static String renderJson(MeesageCallback meesageCallback) {
		Map<String, Object> returnMsg = Maps.newHashMap();
		try {
			meesageCallback.business(returnMsg);
			returnMsg.put("success", true);
		} catch (Exception e) {
			logger.error("[{}] to Error Msg[{}]", JsonActionTemplate.class,
					Throwables.getRootCause(e));
			returnMsg.put("msg", Throwables.getRootCause(e));
			returnMsg.put("success", false);
			e.printStackTrace();
		}
		if (meesageCallback instanceof SingleObjectMeesageCallBack) {
			return meesageCallback.toWriter(returnMsg
					.get(SingleObjectMeesageCallBack.class.getName()));
		}
		return meesageCallback.toWriter(returnMsg);
	}

}
