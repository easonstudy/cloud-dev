package com.cjcx.pay.framework.controller;

import com.cjcx.pay.framework.orm.BaseEntity;
import com.cjcx.pay.framework.service.IBaseService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class BaseController<E extends BaseEntity, Id extends Serializable> extends AdminSupport {

    protected String URL_ADD = getUrlPrefix() + "Add";
    protected String URL_EDIT = getUrlPrefix() + "Edit";
    protected String URL_VIEW = getUrlPrefix() + "View";
    protected String URL_List = getUrlPrefix() + "List";

    private static final Log log = LogFactory.getLog(BaseController.class);
    private static final SimpleDateFormat DEFAULT_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DEFAULT_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected abstract IBaseService<?, Id> getDefaultService();

    private class MyCustomDateEditor extends CustomDateEditor {

        private Class<?> clazz;
        private DateFormat dateFormat;

        public MyCustomDateEditor(Class<?> clazz, DateFormat dateFormat,
                                  boolean allowEmpty) {
            super(dateFormat, allowEmpty);
            this.clazz = clazz;
            this.dateFormat = dateFormat;
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {

            Long time = null;
            if (StringUtils.isNotEmpty(text) && NumberUtils.isNumber(text)) {
                try {
                    time = Long.parseLong(text);
                } catch (Exception ex) {
                }
            }

            if (StringUtils.isNotEmpty(text) && time == null) {
                try {
                    time = this.dateFormat.parse(text).getTime();
                } catch (Exception ex) {
                    log.error(ex);
                }
            }

            if (time != null) {
                if (clazz.equals(Date.class)) {
                    this.setValue(new Date(time));
                } else if (clazz.equals(java.sql.Date.class)) {
                    this.setValue(new java.sql.Date(time));
                } else if (clazz.equals(java.sql.Time.class)) {
                    this.setValue(new java.sql.Time(time));
                } else if (clazz.equals(java.sql.Timestamp.class)) {
                    this.setValue(new java.sql.Timestamp(time));
                }
            }
        }
    }

    /**
     * 数据绑定
     *
     * @param binder WebDataBinder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyCustomDateEditor(Date.class, DEFAULT_DATETIME_FORMAT, false));
        binder.registerCustomEditor(java.sql.Date.class, new MyCustomDateEditor(java.sql.Date.class, DEFAULT_DATE_FORMAT, false));
        binder.registerCustomEditor(java.sql.Time.class, new MyCustomDateEditor(java.sql.Time.class, DEFAULT_TIME_FORMAT, false));
        binder.registerCustomEditor(java.sql.Timestamp.class, new MyCustomDateEditor(java.sql.Timestamp.class, DEFAULT_DATE_FORMAT, false));
    }

    protected String getUrlPrefix() {
        Class<?> clazz = getSuperClassGenricType(this.getClass(), 0);
        if (clazz != null) {
            String className = clazz.getSimpleName();
            if (className.endsWith("Dto")) {
                className = className.substring(0, className.length() - 3);
            }

            return className;
        }
        return "";
    }

    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    public static Class<?> getSuperClassGenricType(final Class<?> clazz, final int index) {
        // 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return null;
        }
        // 返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return null;
        }
        if (!(params[index] instanceof Class)) {
            return null;
        }
        return (Class<?>) params[index];
    }

  	/*@RequestMapping(value = "/delete")
    @ResponseBody
	public Map<String, Object> delete(HttpServletResponse response,@RequestParam Id id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			getDefaultService().deleteById(id);
			map.put("flag", "succ");
			map.put("msg", "删除成功！");
		} catch (Exception e) {
			log.error("", e);
			map.put("flag", "error");
			map.put("msg", "删除失败！");
		}
		return map;
	}*/


}
