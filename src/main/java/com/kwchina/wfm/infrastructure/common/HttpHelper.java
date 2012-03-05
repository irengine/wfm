package com.kwchina.wfm.infrastructure.common;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.organization.dto.ErrorDTO;

public class HttpHelper {
	
	public static void output(HttpServletResponse response, String result) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().print(result);
			response.flushBuffer();
		} catch (IOException e) {
		}
	}
	
	public static void output(HttpServletResponse response, ErrorDTO error) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().print(JacksonHelper.getJson(error));
			response.flushBuffer();
		} catch (IOException e) {
		}
	}
}
