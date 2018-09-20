Spring boot启动流程
日志
LoggerContext 

启动Servlet引擎
Starting Servlet Engine: Apache Tomcat/8.5.29

注册Servlet容器 使用注解@WebServlet
ServletRegistrationBean - Mapping servlet: 'dispatcherServlet' to [/]

过滤器  使用注解@WebFilter
FilterRegistrationBean Mapping filter: 'characterEncodingFilter' to: [/*]
FilterRegistrationBean Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
FilterRegistrationBean Mapping filter: 'httpPutFormContentFilter' to: [/*]
FilterRegistrationBean Mapping filter: 'requestContextFilter' to: [/*]
FilterRegistrationBean Mapping filter: 'druidWebStatFilter' to urls: [/*]

配置参数 使用注解@Configuare @Bean的注入

初始化默认线程池

SpringMVC RequestMappingHandlerMapping

开始Tomcat nio
Http11NioProtocol - Starting ProtocolHandler ["http-nio-8083"]
NioSelectorPool - Using a shared selector for servlet write/read