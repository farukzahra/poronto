package br.fmz.poronto.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Contexto implements ServletContextListener {

	public void contextInitialized(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();
		try {
			String arqConf = servletContext.getRealPath("")
					+ "/WEB-INF/configuracao.properties";
			Configuracao.getInstance().loadConfig(arqConf);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {
		System.out.println("Destruindo o contexto");

	}
}
