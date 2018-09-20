package com.cjcx.pay.framework.web;

import java.io.Serializable;

public class PageParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2960398574777651921L;
	
	/**
	 * 当前页
	 */
	private int pageNum = 1;

	/**
	 * 每页记录数
	 */
	private int pageSize = 20;

	/**
	 * 总记录数
	 */
	private long total = 0;

	/**
	 * 总页数
	 */
	private int pages = 0;
	
	/**
	 * 是否启用分页
	 */
	private boolean enabled = false;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
}
